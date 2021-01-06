package madspild.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madspild.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;

import madspild.Activities.MainActivity;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.HttpClient.HttpClient;
import madspild.Models.User;

public class LoginFragment extends Fragment{

    TextInputLayout usernameTextInput; //username
    TextInputEditText usernameEditText;

    TextInputLayout passwordTextInput; //password
    TextInputEditText passwordEditText;

    MaterialButton loginButton;
    TextView loginCreateAcountText;

    AuthenticationClient authenticationClient;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.fragment_login, container, false);
        authenticationClient = new AuthenticationClient();
        if(HttpClientHelper.getToken() != null){
            getUserInformation();
        }

        //til at teste login/registrer ved startup af appen
        final TextInputLayout usernameTextInput = view.findViewById(R.id.login_username_text_input); //username
        final TextInputEditText usernameEditText = view.findViewById(R.id.login_username_edit_text);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.login_password_text_input); //password
        final TextInputEditText passwordEditText = view.findViewById(R.id.login_password_edit_text);

        loginButton = view.findViewById(R.id.login_button);
        loginCreateAcountText = view.findViewById(R.id.login_create_account_text);

        initEvents();

        return view;
    }

    private void initEvents(){
        loginButton.setOnClickListener(view -> {
            String username = "missekat";
            String password = "missekat";
            authenticationClient.login(username, password, (resp) -> {
                getUserInformation();
            }, (respError) -> {
                System.out.println(respError);
            });
        });

        loginCreateAcountText.setOnClickListener(view -> {
            Fragment fragment = new RegisterFragment();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private void getUserInformation(){
        authenticationClient.getUserInformation((resp1) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                User user = mapper.readValue(resp1, User.class);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        },(respError) -> {
            System.out.println(respError);
        });
    }

}

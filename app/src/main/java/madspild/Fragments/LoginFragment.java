package madspild.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madspild.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import madspild.Activities.MainActivity;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.Models.User;

public class LoginFragment extends Fragment {

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
            checkUserAccess();
        }


        //til at teste login/registrer ved startup af appen
         usernameTextInput = (TextInputLayout)view.findViewById(R.id.login_username_text_input); //username
          usernameEditText = (TextInputEditText)view.findViewById(R.id.login_username_edit_text);

          passwordTextInput = (TextInputLayout)view.findViewById(R.id.login_password_text_input); //password
          passwordEditText = (TextInputEditText)view.findViewById(R.id.login_password_edit_text);

        usernameTextInput.getEditText().setText("test1");
        passwordTextInput.getEditText().setText("test1");

        loginButton = view.findViewById(R.id.activity_start_createuser);
        loginCreateAcountText = view.findViewById(R.id.login_create_account_text);

        initEvents();

        return view;
    }

    private void initEvents(){


        loginButton.setOnClickListener(view -> {


            String username = Objects.requireNonNull(usernameTextInput.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordTextInput.getEditText()).getText().toString();

            authenticationClient.login(username, password, (respObject) -> {
                checkUserAccess();
            }, (respError) -> {
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(getActivity(), respError, Toast.LENGTH_SHORT).show();
                });
            });


        });

        loginCreateAcountText.setOnClickListener(view -> {
            Fragment fragment = new RegisterFragment();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_start_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private void checkUserAccess(){
        authenticationClient.getUserInformation((respObject) -> {
            HttpClientHelper.user = (User) respObject;

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        },(respError) -> {
            Log.println(Log.ERROR, "AUTHENTICATION", respError);
        });
    }

}

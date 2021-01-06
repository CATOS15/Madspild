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
import madspild.HttpClient.HttpClient;
import madspild.Models.User;

public class LoginFragment extends Fragment{

    MaterialButton loginbutton;
    TextInputLayout usernameTextInput; //username
    TextInputEditText usernameEditText;

    TextInputLayout passwordTextInput; //password
    TextInputEditText passwordEditText;
    TextView login_create_account_text;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.fragment_login, container, false);

        //til at teste login/registrer ved startup af appen
        final TextInputLayout usernameTextInput = view.findViewById(R.id.login_username_text_input); //username
        final TextInputEditText usernameEditText = view.findViewById(R.id.login_username_edit_text);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.login_password_text_input); //password
        final TextInputEditText passwordEditText = view.findViewById(R.id.login_password_edit_text);

        login_create_account_text = view.findViewById(R.id.login_create_account_text);

        MaterialButton loginbutton = view.findViewById(R.id.login_button);

        HttpClient httpClient = new HttpClient(getContext());
        if(httpClient.getToken() != null){
            getUserInformation(httpClient);
        }

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.ms_error_password));
                }
                if (!isUsernameValid(usernameEditText.getText())) {
                    usernameTextInput.setError(getString(R.string.ms_error_username_empty));
                }
                else {
                    String username = "missekat";
                    String password = "missekat";
                    httpClient.login(username, password, (resp) -> {
                        getUserInformation(httpClient);
                    }, (respError) -> {
                        //noinspection Convert2MethodRef
                        System.out.println(respError);
                    });
                }
            }
        });

        login_create_account_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
        return view;
    }

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }
    private boolean isUsernameValid(@Nullable Editable text) {
        return text != null && text.length() >= 0;
    }

    private void getUserInformation(HttpClient httpClient){
        httpClient.getUserInformation((resp1) -> {
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
            //noinspection Convert2MethodRef
            System.out.println(respError);
        });
    }

}

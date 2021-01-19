package madspild.Fragments;

import android.app.Activity;
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

import io.sentry.Sentry;
import madspild.Activities.MainActivity;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.Models.User;

public class RegisterFragment extends Fragment{

    MaterialButton createUserButton;
    TextInputLayout usernameTextInput; //username
    TextInputEditText usernameEditText;

    TextInputLayout passwordTextInput; //password
    TextInputEditText passwordEditText;

    TextView register_already_registered_text;

    TextInputLayout firstnameTextInput; //firstname
    TextInputLayout lastnameTextInput; //lastname
    TextInputLayout emailTextInput; //email
    TextInputLayout phoneTextInput; //phone
    Activity activity;
    String respondAPI;
    AuthenticationClient authenticationClient;


    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.fragment_register, container, false);

        register_already_registered_text = view.findViewById(R.id.activity_start_alreadyregistrered);
        authenticationClient = new AuthenticationClient();
        //til at teste login/registrer ved startup af appen

        activity = getActivity();
        usernameTextInput = view.findViewById(R.id.activity_start_username); //username
        usernameEditText = view.findViewById(R.id.login_username_edit_text);

        passwordTextInput = view.findViewById(R.id.activity_start_password); //password
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        createUserButton = view.findViewById(R.id.activity_start_createuser);

       firstnameTextInput = view.findViewById(R.id.activity_start_firstname); //firstname

      lastnameTextInput = view.findViewById(R.id.activity_start_lastname); //lastname

        emailTextInput = view.findViewById(R.id.activity_start_email); //email

        phoneTextInput = view.findViewById(R.id.activity_start_phone); //email




        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    User user = new User();
                    user.setUsername(usernameTextInput.getEditText().getText().toString());
                    user.setPassword(passwordTextInput.getEditText().getText().toString());
                    user.setFirstname(firstnameTextInput.getEditText().getText().toString());
                    user.setLastname(lastnameTextInput.getEditText().getText().toString());
                    user.setEmail(emailTextInput.getEditText().getText().toString());
                    user.setPhone(phoneTextInput.getEditText().getText().toString());
                    user.setAdmin(false);
                    user.setFamilyid(null);
                    user.setId(null);


                if(validate(user))
                    {
                        authenticationClient.createUser(user, (respObject) -> {

                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getActivity(), "Brugeren er blevet oprettet", Toast.LENGTH_SHORT).show();
                            });
                            System.out.println("User been created");

                            checkUserAccess();

                        }, (respError) -> {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Toast.makeText(getActivity(), respError, Toast.LENGTH_SHORT).show();
                                Sentry.captureMessage("Error - failed creating a new user");
                            });
                        });


                    }

                }
        });


        register_already_registered_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_start_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    private void checkUserAccess(){
        authenticationClient.getUserInformation((respObject) -> {
            HttpClientHelper.user = (User) respObject;

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        },(respError) -> {
            Log.println(Log.ERROR, "AUTHENTICATION", respError);
            Sentry.captureMessage("Error - failed retreiving user information");
        });
    }

    public boolean validate(User user)
    {
        if (user.getUsername().length() < 4){
            usernameTextInput.setError("Brugernavn skal være mindst 4 lang");
            return false;}
        else{usernameTextInput.setError(null);}
        if(user.getPassword().length() < 5 ){
            passwordTextInput.setError("Password skal være mindst 5 lang");
            return false;}
        else{passwordTextInput.setError(null);}
        if(user.getFirstname().length() < 2 ){
            firstnameTextInput.setError("Fornavn skal være mindst 2 lang");
            return false;}
        else{firstnameTextInput.setError(null);}
        if(user.getLastname().length() < 2 ){
            lastnameTextInput.setError("Efternavn skal være mindst 2 lang");
            return false;}
        else{lastnameTextInput.setError(null);}
        if(!(user.getEmail().contains("@")) || !(user.getEmail().contains("."))){
            emailTextInput.setError("Email skal indeholde @ og .");
            return false;}
        else{emailTextInput.setError(null);}
        if(user.getPhone().length() != 8 || user.getPhone().contains(" ")){
            phoneTextInput.setError("Telefon skal indeholde 8 tal og ingen mellemrum");
            return false;}
        else
        {try{Integer.parseInt(user.getPhone()); phoneTextInput.setError(null);}
        catch (Exception e){
            phoneTextInput.setError("Telefon skal indeholde 8 tal og ingen mellemrum");
            return false;} }

        return true;
    }





}

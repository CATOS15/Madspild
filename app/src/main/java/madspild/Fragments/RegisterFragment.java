package madspild.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madspild.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import madspild.HttpClient.AuthenticationClient;
import madspild.Models.User;

public class RegisterFragment extends Fragment{

    MaterialButton loginbutton;
    TextInputLayout usernameTextInput; //username
    TextInputEditText usernameEditText;

    TextInputLayout passwordTextInput; //password
    TextInputEditText passwordEditText;

    TextView register_already_registered_text;

    TextInputLayout firstnameTextInput; //firstname
    TextInputLayout lastnameTextInput; //lastname
    TextInputLayout emailTextInput; //email
    TextInputLayout phoneTextInput; //phone
    User user;

    AuthenticationClient authenticationClient;


    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.fragment_register, container, false);

        register_already_registered_text = view.findViewById(R.id.register_already_registered_text);
        authenticationClient = new AuthenticationClient();
        //til at teste login/registrer ved startup af appen

        usernameTextInput = view.findViewById(R.id.register_username_text_input); //username
        usernameEditText = view.findViewById(R.id.login_username_edit_text);

        passwordTextInput = view.findViewById(R.id.register_password_text_input); //password
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        loginbutton = view.findViewById(R.id.login_button);

       firstnameTextInput = view.findViewById(R.id.register_firstname_text_input); //firstname

      lastnameTextInput = view.findViewById(R.id.register_lastname_text_input); //lastname

        emailTextInput = view.findViewById(R.id.register_email_text_input); //email

        phoneTextInput = view.findViewById(R.id.register_phone_text_input); //email



        loginbutton.setOnClickListener(new View.OnClickListener() {
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


                    if(user.validate())
                    {
                        authenticationClient.createUser(user, (respObject) -> {
                            System.out.println("User been created");
                        }, (respError) -> {
                            Log.println(Log.ERROR, "AUTHENTICATION", respError);
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
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public boolean validate(User user)
    {
        if (user.getUsername().length() < 4){
//            usernameEditText.setError("Fejl");
            return false;}
        if(user.getPassword().length() < 5 ){return false;}
        if(user.getFirstname().length() < 2 ){return false;}
        if(user.getLastname().length() < 2 ){return false;}
        if(!(user.getEmail().contains("@")) ){return false;}
        if(user.getPhone().length() <= 7 || user.getPhone().length() >= 9 || user.getPhone().contains(" ")){
            return false;}
        else
        {try{Integer.parseInt(user.getPhone());}
        catch (Exception e){return false;} }

        return true;
    }




}

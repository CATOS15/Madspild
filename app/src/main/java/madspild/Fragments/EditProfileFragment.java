package madspild.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class EditProfileFragment extends Fragment {

    TextInputLayout editprofile_text_firstname; //fornavn
    TextInputEditText editprofile_edit_firstname;

    TextInputLayout editprofile_text_lastname; //efternavn
    TextInputEditText editprofile_edit_lastname;

    TextInputLayout editprofile_text_phonenumber; //telefon nummer
    TextInputEditText editprofile_edit_phonenumber;

    TextInputLayout editprofile_text_email; //e-mail
    TextInputEditText editprofile_edit_email;

    TextView editprofile_text_username; //brugernavn

    Button editprofile_button;
    Button cancel_button;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.fragment_edit_profile, container, false);


        //Henter views
        editprofile_text_firstname = view.findViewById(R.id.editprofile_text_firstname);
        editprofile_edit_firstname = view.findViewById(R.id.editprofile_edit_firstname);

        editprofile_text_lastname = view.findViewById(R.id.editprofile_text_lastname);
        editprofile_edit_lastname = view.findViewById(R.id.editprofile_edit_lastname);

        editprofile_text_phonenumber = view.findViewById(R.id.editprofile_text_phonenumber);
        editprofile_edit_phonenumber = view.findViewById(R.id.editprofile_edit_phonenumber);

        editprofile_text_email = view.findViewById(R.id.editprofile_text_email);
        editprofile_edit_email = view.findViewById(R.id.editprofile_edit_email);

        editprofile_text_username = view.findViewById(R.id.editprofile_text_username);

        editprofile_button = view.findViewById(R.id.editprofile_button);
        cancel_button = view.findViewById(R.id.cancel_button);


        //Sæt brugerinfo
        editprofile_text_firstname.getEditText().setText(HttpClientHelper.user.getFirstname());
        editprofile_text_lastname.getEditText().setText(HttpClientHelper.user.getLastname());
        editprofile_text_phonenumber.getEditText().setText(HttpClientHelper.user.getPhone());
        editprofile_text_email.getEditText().setText(HttpClientHelper.user.getEmail());
        editprofile_text_username.setText(HttpClientHelper.user.getUsername());

        editprofile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClientHelper.user.setFirstname(editprofile_edit_firstname.getText().toString());
                HttpClientHelper.user.setLastname(editprofile_edit_lastname.getText().toString());
                HttpClientHelper.user.setPhone(editprofile_edit_phonenumber.getText().toString());
                HttpClientHelper.user.setEmail(editprofile_edit_email.getText().toString());


                AuthenticationClient authenticationClient = new AuthenticationClient();
                authenticationClient.editUser(HttpClientHelper.user, (respObject) -> {
                }, (respError) -> {
                    System.out.println(respError);
                });
            }

        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShowProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;


    }


}

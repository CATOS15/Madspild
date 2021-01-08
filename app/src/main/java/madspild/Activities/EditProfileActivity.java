package madspild.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.madspild.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import madspild.Fragments.LoginFragment;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.HttpClient.Config.HttpClient;

public class EditProfileActivity extends AppCompatActivity {

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);



        //Henter views
        editprofile_text_firstname = findViewById(R.id.editprofile_text_firstname);
        editprofile_edit_firstname = findViewById(R.id.editprofile_edit_firstname);

        editprofile_text_lastname = findViewById(R.id.editprofile_text_lastname);
        editprofile_edit_lastname = findViewById(R.id.editprofile_edit_lastname);

        editprofile_text_phonenumber = findViewById(R.id.editprofile_text_phonenumber);
        editprofile_edit_phonenumber = findViewById(R.id.editprofile_edit_phonenumber);

        editprofile_text_email = findViewById(R.id.editprofile_text_email);
        editprofile_edit_email = findViewById(R.id.editprofile_edit_email);

        editprofile_text_username = findViewById(R.id.editprofile_text_username);

        editprofile_button = findViewById(R.id.editprofile_button);


        //Sæt brugerinfo
        editprofile_text_firstname.getEditText().setText(HttpClientHelper.user.getFirstname());
        editprofile_text_lastname.getEditText().setText(HttpClientHelper.user.getLastname());
        editprofile_text_phonenumber.getEditText().setText(HttpClientHelper.user.getPhone());
        editprofile_text_email.getEditText().setText(HttpClientHelper.user.getEmail());
        editprofile_text_username.setText(HttpClientHelper.user.getUsername());

        editprofile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClientHelper.user.setFirstname(editprofile_edit_firstname.getText().toString() );
                HttpClientHelper.user.setLastname(editprofile_edit_lastname.getText().toString() );
                HttpClientHelper.user.setPhone(editprofile_edit_phonenumber.getText().toString() );
                HttpClientHelper.user.setEmail(editprofile_edit_email.getText().toString());


                AuthenticationClient authenticationClient = new AuthenticationClient();
                authenticationClient.editUser(HttpClientHelper.user, (respObject) -> {
                },(respError) -> {
                    System.out.println(respError);
                });
            }
        });


    }

    public void gotToLevel(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CAME_FROM_EDIT_PROFILE", true);
        startActivity(intent);
    }
}



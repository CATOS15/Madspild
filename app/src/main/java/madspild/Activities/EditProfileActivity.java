package madspild.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madspild.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.Models.User;

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

    Button editprofile_button_edit;
    Button editprofile_button_save;
    Button editprofile_button_cancel;
    Button editprofile_button_backbutton;

    LinearLayout editprofile_linearlayout_inputfields;

    TableRow editprofile_tablerow_buttons_edit;
    TableRow editprofile_tablerow_buttons_cancelsave;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        );
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

        editprofile_button_edit = findViewById(R.id.editprofile_button_edit);
        editprofile_button_cancel = findViewById(R.id.editprofile_button_cancel);
        editprofile_button_save = findViewById(R.id.editprofile_button_save);
        editprofile_button_backbutton = findViewById(R.id.editprofile_button_backbutton);

        editprofile_linearlayout_inputfields = findViewById(R.id.editprofile_linearlayout_inputfields);

        editprofile_tablerow_buttons_edit = findViewById(R.id.editprofile_tablerow_buttons_edit);
        editprofile_tablerow_buttons_cancelsave = findViewById(R.id.editprofile_tablerow_buttons_cancelsave);

        // Diable input fields
        for ( int j = 0; j < editprofile_linearlayout_inputfields.getChildCount();  j++ ){
            View textfield = editprofile_linearlayout_inputfields.getChildAt(j);
            textfield.setEnabled(false);
        }

        //Sæt brugerinfo
        getSetUser();

        editprofile_button_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editprofile_button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButton(false,true,true);
            }
        });

        editprofile_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showButton(true,false,false);
                getSetUser();
            }
        });

        editprofile_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpClientHelper.user.setFirstname(editprofile_edit_firstname.getText().toString());
                HttpClientHelper.user.setLastname(editprofile_edit_lastname.getText().toString());
                HttpClientHelper.user.setPhone(editprofile_edit_phonenumber.getText().toString());
                HttpClientHelper.user.setEmail(editprofile_edit_email.getText().toString());

                if(validate(HttpClientHelper.user)){
                    AuthenticationClient authenticationClient = new AuthenticationClient();
                    authenticationClient.editUser(HttpClientHelper.user, (respObject) -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            showButton(true,false,false);
                        });
                    }, (respError) -> {
                        System.out.println(respError);
                    });
                }
            }
        });


    }


    public void showButton(boolean showEditButton,boolean showCancelSaveButton,boolean fieldsEditable){
        if(showEditButton){
            editprofile_tablerow_buttons_edit.setVisibility(View.VISIBLE);
        } else {
            editprofile_tablerow_buttons_edit.setVisibility(View.GONE);
        }

        if(showCancelSaveButton){
            editprofile_tablerow_buttons_cancelsave.setVisibility(View.VISIBLE);
        } else {
            editprofile_tablerow_buttons_cancelsave.setVisibility(View.GONE);
        }

        for ( int j = 0; j < editprofile_linearlayout_inputfields.getChildCount();j++){
            View textfield = editprofile_linearlayout_inputfields.getChildAt(j);
            textfield.setEnabled(fieldsEditable);
        }
    }

    public boolean validate(User user)
    {
        if(user.getFirstname().length() < 2 ){
            editprofile_text_firstname.setError("Fornavn skal være midnst 2 lang");
            return false;}
        else{editprofile_text_firstname.setError(null);}
        if(user.getLastname().length() < 2 ){
            editprofile_text_lastname.setError("Efternavn skal være midnst 2 lang");
            return false;}
        else{editprofile_text_lastname.setError(null);}
        if(!(user.getEmail().contains("@")) || !(user.getEmail().contains("."))){
            editprofile_text_email.setError("Email skal indeholde @ og .");
            return false;}
        else{editprofile_text_email.setError(null);}
        if(user.getPhone().length() != 8 || user.getPhone().contains(" ")){
            editprofile_text_phonenumber.setError("Telefon skal indeholde 8 tal og ingen mellemrum");
            return false;}
        else
        {try{Integer.parseInt(user.getPhone()); editprofile_text_phonenumber.setError(null);}
        catch (Exception e){
            editprofile_text_phonenumber.setError("Telefon skal indeholde 8 tal og ingen mellemrum");
            return false;} }

        return true;
    }

    public void getSetUser(){
        editprofile_text_firstname.getEditText().setText(HttpClientHelper.user.getFirstname());
        editprofile_text_lastname.getEditText().setText(HttpClientHelper.user.getLastname());
        editprofile_text_phonenumber.getEditText().setText(HttpClientHelper.user.getPhone());
        editprofile_text_email.getEditText().setText(HttpClientHelper.user.getEmail());
    }

}



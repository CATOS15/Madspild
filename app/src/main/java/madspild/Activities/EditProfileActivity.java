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
import madspild.Fragments.ShowProfileFragment;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.HttpClient.Config.HttpClient;

public class EditProfileActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ShowProfileFragment())
                .commit();

    }

}



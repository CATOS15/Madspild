package madspild.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.madspild.R;
import madspild.Fragments.LoginFragment;
import madspild.Helpers.HttpClientHelper;

public class EditProfileActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        ImageButton backbutton = findViewById(R.id.backButton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           // gotToLevel();
                onBackPressed();
            }
        });

    }
    public void gotToLevel(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CAME_FROM_EDIT_PROFILE", true);
        startActivity(intent);
    }
}



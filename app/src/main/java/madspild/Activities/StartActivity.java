package madspild.Activities;

import com.example.madspild.R;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import madspild.Fragments.LoginFragment;
import madspild.Helpers.HttpClientHelper;

public class StartActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        HttpClientHelper.init(getBaseContext());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }

    }
}

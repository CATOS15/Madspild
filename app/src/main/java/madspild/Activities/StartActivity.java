package madspild.Activities;

import com.example.madspild.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import madspild.Fragments.LoginFragment;
import madspild.Helpers.HttpClientHelper;

public class StartActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FrameLayout loadingContent = findViewById(R.id.loading_content);

        HttpClientHelper.init(getBaseContext());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
        }

        HttpClientHelper.setListener(loading -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                loadingContent.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
            });
        });
    }
}

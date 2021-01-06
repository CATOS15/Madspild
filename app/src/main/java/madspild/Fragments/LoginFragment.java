package madspild.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madspild.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.fragment_login, container, false);

        final TextInputLayout usernameTextInput = view.findViewById(R.id.login_username_text_input); //username
        final TextInputEditText usernameEditText = view.findViewById(R.id.login_username_edit_text);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.login_password_text_input); //password
        final TextInputEditText passwordEditText = view.findViewById(R.id.login_password_edit_text);
        MaterialButton loginbutton = view.findViewById(R.id.login_button);

        // Sets errors if inputs are wrong. password is less than 8 character, empty username
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
                    return;
                    /*passwordTextInput.setError(null); // Clear the error
                    ((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false); // Navigate to the next Fragment
                    */
                }

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
}

package madspild.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.madspild.R;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    ImageView settingsicon;
    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsicon = (ImageView) getView().findViewById(R.id.settingsIcon);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new LoginFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(container, new EditProfileFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    /* public void onClick(View v) {
        if (v.getId() == R.id.settingsIcon) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.layout.fragment_profile, new EditProfileFragment())
                    .commit();

        }
} */
}
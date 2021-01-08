package madspild.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.madspild.R;

import java.util.Objects;

import madspild.Activities.StartActivity;
import madspild.Helpers.HttpClientHelper;


public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView settingIcon = view.findViewById(R.id.settingsIcon);
        settingIcon.setOnClickListener((event) -> {
            ViewPager2 viewPager = view.getRootView().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(3);
        });

        view.findViewById(R.id.logout_button).setOnClickListener((event) -> {
            HttpClientHelper.removeToken();
            Intent intent = new Intent(getActivity(), StartActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });

        return view;
    }
}
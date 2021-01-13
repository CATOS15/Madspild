package madspild.Helpers;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;

import madspild.Fragments.OverviewFragment;
import madspild.Fragments.ProfileFragment;
import madspild.Fragments.ScanFragment;

//This is used for mapnings
public class MenuHelper {
    public static final int NUM_PAGES = 3;

    public static int getMenuNumberFromId(int id){
        switch (id) {
            case R.id.fragment_navigation_overview:
                return 0;
            case R.id.fragment_navigation_scan:
                return 1;
            case R.id.fragment_navigation_profile:
                return 2;
        }
        return 1;
    }
    public static int getMenuIdFromNumber(int number){
        switch (number) {
            case 0:
                return R.id.fragment_navigation_overview;
            case 1:
                return R.id.fragment_navigation_scan;
            case 2:
                return R.id.fragment_navigation_profile;
        }
        return R.id.fragment_navigation_scan;
    }

    public static Fragment getMenuFragmentFromNumber(int number){
        switch(number) {
            case 0:
                return new OverviewFragment();
            case 1:
                return new ScanFragment();
            case 2:
                return new ProfileFragment();
        }
        return new ScanFragment();
    }
}

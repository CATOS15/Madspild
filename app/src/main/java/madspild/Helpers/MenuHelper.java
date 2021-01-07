package madspild.Helpers;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;

import madspild.Fragments.OverviewFragment;
import madspild.Fragments.EditProfileFragment;
import madspild.Fragments.ScanFragment;

//This is used for mapnings
public class MenuHelper {
    public static final int NUM_PAGES = 3;

    public static int getMenuNumberFromId(int id){
        switch (id) {
            case R.id.overviewMenuItem:
                return 0;
            case R.id.scanMenuItem:
                return 1;
            case R.id.profileMenuItem:
                return 2;
        }
        return 1;
    }
    public static int getMenuIdFromNumber(int number){
        switch (number) {
            case 0:
                return R.id.overviewMenuItem;
            case 1:
                return R.id.scanMenuItem;
            case 2:
                return R.id.profileMenuItem;
        }
        return R.id.scanMenuItem;
    }

    public static Fragment getMenuFragmentFromNumber(int number){
        switch(number) {
            case 0:
                return new OverviewFragment();
            case 1:
                return new ScanFragment();
            case 2:
                return new EditProfileFragment();
        }
        return new ScanFragment();
    }
}

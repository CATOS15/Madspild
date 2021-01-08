package madspild.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import madspild.Helpers.MenuHelper;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MenuHelper.getMenuFragmentFromNumber(position);
    }

    @Override
    public int getItemCount() {
        return MenuHelper.NUM_PAGES;
    }
}
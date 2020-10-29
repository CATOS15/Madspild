package com.example.madspild;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.madspild.OverviewFragement;
import com.example.madspild.ProfileFragment;
import com.example.madspild.ScanFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    public SimpleFragmentPagerAdapter(
            FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0) {
            return new ProfileFragment();
        }
        else if (position == 1) {
            return new ScanFragment();
        }
        else {
            return new OverviewFragement();
        }
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}

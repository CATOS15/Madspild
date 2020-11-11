package com.example.madspild;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends FragmentActivity {
    BottomNavigationView navigation;
    static final int NUM_PAGES = 3;
    ViewPager2 viewPager;
    FragmentStateAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_buttom_nav);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        // Setup bottom navigation
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.scanMenuItem);


        // Triggers if new page is selected + set bottomitem selected
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigation.setSelectedItemId(R.id.overviewMenuItem);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.scanMenuItem);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.profileMenuItem);
                        break;
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.overviewMenuItem:
                    viewPager.setCurrentItem(0,true);
                    return true;
                case R.id.scanMenuItem:
                    viewPager.setCurrentItem(1,true);
                    return true;
                case R.id.profileMenuItem:
                    viewPager.setCurrentItem(2,true);
                    return true;
            }
            return false;
        }
    };

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0:
                    return new OverviewFragment();
                case 1:
                    return new ScanFragment();
                case 2:
                    return new ProfileFragment();
            }
            return new ScanFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}

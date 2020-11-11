package madspild.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madspild.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

import madspild.Adapters.ScreenSlidePagerAdapter;
import madspild.Helpers.MenuHelper;


public class MainActivity extends FragmentActivity {
    private Deque<Integer> pageStack = new ArrayDeque<>();
    private ViewPager2 viewPager;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.scanMenuItem);

        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));
        viewPager.setCurrentItem(1);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(MenuHelper.getMenuIdFromNumber(position));
            }
        });
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuNumber = MenuHelper.getMenuNumberFromId(item.getItemId());
                if(viewPager.getCurrentItem() != menuNumber){
                    pageStack.add(menuNumber);
                    viewPager.setCurrentItem(menuNumber);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pageStack.isEmpty()) {
            finish();
        } else {
            viewPager.setCurrentItem(pageStack.pop());
        }
    }
}

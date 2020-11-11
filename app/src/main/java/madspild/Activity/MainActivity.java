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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madspild.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

import madspild.Adapters.ScreenSlidePagerAdapter;
import madspild.Fragments.OverviewFragment;
import madspild.Fragments.ProfileFragment;
import madspild.Fragments.ScanFragment;
import madspild.Helpers.MenuHelper;


public class MainActivity extends FragmentActivity {
    private Deque<Integer> pageStack = new ArrayDeque<>();
    private ViewPager2 viewPager;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},1);
        }

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

    //TODO Når man klikker at man gerne må bruge kameraet
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Todo");
                //Permission granted!
            } else {
                System.out.println("Todo");
                //Permission denied!
            }
        }
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

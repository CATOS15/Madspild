package madspild.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madspild.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

import madspild.Adapters.ScreenSlidePagerAdapter;
import madspild.Helpers.HttpClientHelper;
import madspild.Helpers.MenuHelper;


public class MainActivity extends FragmentActivity {
    private int lastPagePosition = 1;
    private Stack<Integer> pageStack = new Stack<>();

    private ViewPager2 viewPager;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        navigation = findViewById(R.id.bottom_navigation_view);

        navigation.setSelectedItemId(R.id.scanMenuItem);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));
        viewPager.setCurrentItem(1);

        initEvents();

        FrameLayout loadingContent = findViewById(R.id.loading_content_main);
        HttpClientHelper.setListener(loading -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                loadingContent.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
            });
        });
    }

    private void initEvents(){
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(lastPagePosition != position){
                    pageStack.add(lastPagePosition);
                }
                navigation.setSelectedItemId(MenuHelper.getMenuIdFromNumber(position));

                lastPagePosition = position;
            }
        });
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager.setCurrentItem(MenuHelper.getMenuNumberFromId(item.getItemId()));
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pageStack.isEmpty()) {
            finish();
        } else {
            lastPagePosition = pageStack.pop();
            viewPager.setCurrentItem(lastPagePosition);
        }
    }
}
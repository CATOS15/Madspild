package madspild.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madspild.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Stack;

import madspild.Adapters.ScreenSlidePagerAdapter;
import madspild.Helpers.MenuHelper;
import madspild.Models.User;


public class MainActivity extends FragmentActivity {
    private int lastPagePosition = 1;
    private Stack<Integer> pageStack = new Stack<>();

    private ViewPager2 viewPager;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //til at teste login/registrer ved startup af appen
        setContentView(R.layout.fragment_register);
        //setContentView(R.layout.fragment_login);

        /*
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        navigation = findViewById(R.id.bottom_navigation_view);

        navigation.setSelectedItemId(R.id.scanMenuItem);
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));
        viewPager.setCurrentItem(1);

        initEvents();
*/

        //Lige nu er database delen inaktiv for at undgå at spilde plads på databasen
        //den vil blive sat aktiv når vi skal til at arbejde dybere med databasen

        //Database
//        User user = new User();
//        user.setPasswords("11");
//        user.setEmail("12333");
//        user.setUsername("test");
//        user.setUserID(1);
//
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("user");


//        myRef.child("user").push().setValue(user);
//        database.setValue("Hello, World!");


// Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
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

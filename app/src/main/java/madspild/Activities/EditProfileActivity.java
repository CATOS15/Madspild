package madspild.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.madspild.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.Models.User;

public class EditProfileActivity extends AppCompatActivity {

    TextInputLayout editprofile_text_firstname; //fornavn
    TextInputEditText editprofile_edit_firstname;

    TextInputLayout editprofile_text_lastname; //efternavn
    TextInputEditText editprofile_edit_lastname;

    TextInputLayout editprofile_text_phonenumber; //telefon nummer
    TextInputEditText editprofile_edit_phonenumber;

    TextInputLayout editprofile_text_email; //e-mail
    TextInputEditText editprofile_edit_email;

    TextView editprofile_text_username; //brugernavn

    Button editprofile_button_edit;
    Button editprofile_button_save;
    Button editprofile_button_cancel;
    Button editprofile_button_backbutton;

    LinearLayout editprofile_linearlayout_inputfields;

    TableRow editprofile_tablerow_buttons_edit;
    TableRow editprofile_tablerow_buttons_cancelsave;

    MaterialToolbar activity_editprofile_toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        createnotification();

        // Hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        );
        //Henter views
        editprofile_text_firstname = findViewById(R.id.editprofile_text_firstname);
        editprofile_edit_firstname = findViewById(R.id.editprofile_edit_firstname);

        editprofile_text_lastname = findViewById(R.id.editprofile_text_lastname);
        editprofile_edit_lastname = findViewById(R.id.editprofile_edit_lastname);

        editprofile_text_phonenumber = findViewById(R.id.editprofile_text_phonenumber);
        editprofile_edit_phonenumber = findViewById(R.id.editprofile_edit_phonenumber);

        editprofile_text_email = findViewById(R.id.editprofile_text_email);
        editprofile_edit_email = findViewById(R.id.editprofile_edit_email);

        editprofile_text_username = findViewById(R.id.editprofile_text_username);

        editprofile_button_edit = findViewById(R.id.editprofile_button_edit);
        editprofile_button_cancel = findViewById(R.id.activity_editprofile_cancel);
        editprofile_button_save = findViewById(R.id.activity_editprofile_save);

        editprofile_linearlayout_inputfields = findViewById(R.id.editprofile_linearlayout_inputfields);

        editprofile_tablerow_buttons_edit = findViewById(R.id.editprofile_tablerow_buttons_edit);
        editprofile_tablerow_buttons_cancelsave = findViewById(R.id.editprofile_tablerow_buttons_cancelsave);

        activity_editprofile_toolbar = findViewById(R.id.activity_editprofile_toolbar);

        // Set backbutton
        activity_editprofile_toolbar.setNavigationIcon(R.drawable.icon_backarrow);
        activity_editprofile_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Diable input fields
        for ( int j = 0; j < editprofile_linearlayout_inputfields.getChildCount();  j++ ){
            View textfield = editprofile_linearlayout_inputfields.getChildAt(j);
            textfield.setEnabled(false);
        }

        //Sæt brugerinfo
        getSetUser();


        editprofile_button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButton(false,true,true);
            }
        });

        editprofile_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showButton(true,false,false);
                getSetUser();
            }
        });

        editprofile_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User tempUser = new User();
                tempUser.setFirstname(HttpClientHelper.user.getFirstname());
                tempUser.setLastname(HttpClientHelper.user.getLastname());
                tempUser.setEmail(HttpClientHelper.user.getEmail());
                tempUser.setPhone(HttpClientHelper.user.getPhone());
                tempUser.setFamilyid(HttpClientHelper.user.getFamilyid());
                tempUser.setId(HttpClientHelper.user.getId());
                tempUser.setUsername(HttpClientHelper.user.getUsername());
                tempUser.setPassword(HttpClientHelper.user.getPassword());

                tempUser.setFirstname(editprofile_edit_firstname.getText().toString());
                tempUser.setLastname(editprofile_edit_lastname.getText().toString());
                tempUser.setPhone(editprofile_edit_phonenumber.getText().toString());
                tempUser.setEmail(editprofile_edit_email.getText().toString());


                if(validate(tempUser)){
                    AuthenticationClient authenticationClient = new AuthenticationClient();
                    authenticationClient.editUser(tempUser, (respObject) -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            showButton(true,false,false);
                            HttpClientHelper.user.setFirstname(tempUser.getFirstname());
                            HttpClientHelper.user.setLastname(tempUser.getLastname());
                            HttpClientHelper.user.setPhone(tempUser.getPhone());
                            HttpClientHelper.user.setEmail(tempUser.getEmail());

                            // Snackbar confirmation
                            String respMessage = respObject.toString();
                            Snackbar snackbar = Snackbar.make(getCurrentFocus(),respMessage, 3000);
                            snackbar.show();
                        });
                    }, (respError) -> {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(getApplicationContext(), respError, Toast.LENGTH_SHORT).show();
                        });
                    });
                }
            }
        });


    }


    public void showButton(boolean showEditButton,boolean showCancelSaveButton,boolean fieldsEditable){
        if(showEditButton){
            editprofile_tablerow_buttons_edit.setVisibility(View.VISIBLE);
        } else {
            editprofile_tablerow_buttons_edit.setVisibility(View.GONE);
        }

        if(showCancelSaveButton){
            editprofile_tablerow_buttons_cancelsave.setVisibility(View.VISIBLE);
        } else {
            editprofile_tablerow_buttons_cancelsave.setVisibility(View.GONE);
        }

        for ( int j = 0; j < editprofile_linearlayout_inputfields.getChildCount();j++){
            View textfield = editprofile_linearlayout_inputfields.getChildAt(j);
            textfield.setEnabled(fieldsEditable);
        }
    }

    public boolean validate(User user)
    {
        if(user.getFirstname().length() < 2 ){
            editprofile_text_firstname.setError("Fornavn skal være midnst 2 lang");
            return false;}
        else{editprofile_text_firstname.setError(null);}
        if(user.getLastname().length() < 2 ){
            editprofile_text_lastname.setError("Efternavn skal være midnst 2 lang");
            return false;}
        else{editprofile_text_lastname.setError(null);}
        if(!(user.getEmail().contains("@")) || !(user.getEmail().contains("."))){
            editprofile_text_email.setError("Email skal indeholde @ og .");
            return false;}
        else{editprofile_text_email.setError(null);}
        if(user.getPhone().length() != 8 || user.getPhone().contains(" ")){
            editprofile_text_phonenumber.setError("Telefon skal indeholde 8 tal og ingen mellemrum");
            return false;}
        else
        {try{Integer.parseInt(user.getPhone()); editprofile_text_phonenumber.setError(null);}
        catch (Exception e){
            editprofile_text_phonenumber.setError("Telefon skal indeholde 8 tal og ingen mellemrum");
             return false;} }

        return true;
    }

    public void getSetUser(){
        editprofile_text_firstname.getEditText().setText(HttpClientHelper.user.getFirstname());
        editprofile_text_lastname.getEditText().setText(HttpClientHelper.user.getLastname());
        editprofile_text_phonenumber.getEditText().setText(HttpClientHelper.user.getPhone());
        editprofile_text_email.getEditText().setText(HttpClientHelper.user.getEmail());
    }

    public void createnotification(){
        Intent intent = new Intent(this, MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, opretNotifKanal(this))
            .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
            .setSmallIcon(R.drawable.fragment_navigation_scan)
            //.setSubText("Madspild") //Tekst øverst til højre for ikonet
            .setContentTitle("Du har udløbede madvarer") //Tekst der vises hele tiden
            .setColor(Color.parseColor("#002C6C"))
            //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
            //Tekst når notif ikke er ekspanderet. Kun 1 linje, resten skæres væk
            .setContentText("Gå ind og få et overblik over dine madvarer")
            .setStyle(new NotificationCompat.BigTextStyle()
                    //Tekst når notifikationen er ekspanderet. Teksten kan strække sig over flere linjer.
                    .bigText("Gå ind og få et overblik over dine madvarer"))
            .setVibrate(new long[]{0, 100, 300, 400, 500, 510, 550, 560, 600, 610, 650, 610, -1});

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(42, builder.build());
        /*
        // elementer til hvis der skal være timer på notifikationen
        AlarmManager alarmMgr = null;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);


        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        //setInexactRepeating()
         */
    }

    public static String opretNotifKanal(Context ctx) {
        String KANALID = "kanal_id";
        // Fra API 26 skal man bruge en NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel kanal = new NotificationChannel(KANALID, "kanalnavn", NotificationManager.IMPORTANCE_DEFAULT);
            kanal.setDescription("En notifikationskanal for AndroidElementer (setDescription)");
            ctx.getSystemService(NotificationManager.class).createNotificationChannel(kanal);
        }
        return KANALID;
    }

}



package madspild.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientHelper {
    private static String token = null;
    private static SharedPreferences sharedPreferences = null;

    public static void init(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setToken(String token) {
        HttpClientHelper.token = token;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken() {
        if(token == null){
            if(sharedPreferences != null) token = sharedPreferences.getString("token", null);
        }
        return token;
    }
}

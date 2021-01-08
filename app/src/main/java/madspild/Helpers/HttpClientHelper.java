package madspild.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import madspild.Models.User;

public class HttpClientHelper {
    private static String token = null;
    private static SharedPreferences sharedPreferences = null;
    private static boolean loading = false;
    private static ChangeListener listener;

    public static User user;

    public static boolean isLoading() {
        return loading;
    }

    public static void setLoading(boolean loading) {
        HttpClientHelper.loading = loading;
        if(listener != null) listener.onChange(loading);
    }

    public static ChangeListener getListener() {
        return listener;
    }

    public static void setListener(ChangeListener listener) {
        HttpClientHelper.listener = listener;
    }

    public static void init(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setToken(String token) {
        HttpClientHelper.token = token;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static void removeToken() {
        HttpClientHelper.token = null;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();
    }

    public static String getToken() {
        if(token == null){
            if(sharedPreferences != null) token = sharedPreferences.getString("token", null);
        }
        return token;
    }


    public interface ChangeListener{
        void onChange(boolean loading);
    }
}

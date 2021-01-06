package madspild.HttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import madspild.Helpers.HttpClientHelper;

public abstract class HttpClient {
    protected final String BASE_URL = "http://www.madspild.com/api";
    protected final ObjectMapper mapper;

    public interface RespCallback{
        void onRespCallback(Object resp);
    }

    public interface RespErrorCallback{
        void onRespErrorCallback(String respError);
    }

    public HttpClient(){
        mapper = new ObjectMapper();
    }
}

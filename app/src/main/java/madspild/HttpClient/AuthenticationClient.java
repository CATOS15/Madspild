package madspild.HttpClient;

import android.content.Context;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import madspild.Helpers.HttpClientHelper;

public class AuthenticationClient extends HttpClient {
    public void login(String username, String password, RespCallback respCallback, RespErrorCallback respErrorCallback){
        try {
            OkHttpClient client = new OkHttpClient();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Request request = new Request.Builder()
                    .url(BASE_URL + "/authentication/login")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    respErrorCallback.onRespErrorCallback(e.getMessage());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String responseBody = response.body().string();
                    if(response.code() == 200) {
                        HttpClientHelper.setToken(responseBody);
                        respCallback.onRespCallback("OK");
                    }else{
                        respErrorCallback.onRespErrorCallback(responseBody);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserInformation(RespCallback respCallback, RespErrorCallback respErrorCallback){
        if(HttpClientHelper.getToken() == null){
            respErrorCallback.onRespErrorCallback("Token mangler!");
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/authentication/getUser")
                .header("Authorization", "Bearer " + HttpClientHelper.getToken())
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                respErrorCallback.onRespErrorCallback(e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseBody = response.body().string();
                if(response.code() == 200) {
                    respCallback.onRespCallback(responseBody);
                }else{
                    respErrorCallback.onRespErrorCallback(responseBody);
                }
            }
        });
    }
}

package madspild.HttpClient;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.Config.HttpClient;
import madspild.Models.User;

public class AuthenticationClient extends HttpClient {
    public void login(String username, String password, RespCallback respCallback, RespErrorCallback respErrorCallback){
        try {
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
                        respCallback.onRespCallback("Bruger logget ind");
                    }else{
                        respErrorCallback.onRespErrorCallback(responseBody);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.println(Log.WARN, "JSON", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void createUser(User user, RespCallback respCallback, RespErrorCallback respErrorCallback){
        try {
            String JSON_user = mapper.writeValueAsString(user);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSON_user);
            Request request = new Request.Builder()
                    .url(BASE_URL + "/authentication/createUser")
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
                        respCallback.onRespCallback("Bruger oprettet");
                    }else{
                        respErrorCallback.onRespErrorCallback(responseBody);
                    }
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.println(Log.WARN, "JSON", Objects.requireNonNull(e.getMessage()));
        }
    }


    public void editUser(User user, RespCallback respCallback, RespErrorCallback respErrorCallback){
        try {
            String JSON_user = mapper.writeValueAsString(user);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSON_user);
            Request request = new Request.Builder()
                    .url(BASE_URL + "/authentication/editUser")
                    .header("Authorization", "Bearer " + HttpClientHelper.getToken())
                    .put(body)
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
                        respCallback.onRespCallback("Brugeren blev redigeret");
                    }else{
                        respErrorCallback.onRespErrorCallback(responseBody);
                    }
                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.println(Log.WARN, "JSON", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void getUserInformation(RespCallback respCallback, RespErrorCallback respErrorCallback){
        if(HttpClientHelper.getToken() == null){
            respErrorCallback.onRespErrorCallback("Token mangler!");
        }
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
                    respCallback.onRespCallback(mapper.readValue(responseBody, User.class));
                }else{
                    respErrorCallback.onRespErrorCallback(responseBody);
                }
            }
        });
    }
}

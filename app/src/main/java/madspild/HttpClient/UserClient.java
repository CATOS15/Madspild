package madspild.HttpClient;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import madspild.Helpers.HttpClientHelper;
import madspild.Models.User;

public class UserClient extends HttpClient{

    public void getUsers(HttpClient.RespCallback respCallback, HttpClient.RespErrorCallback respErrorCallback){
        if(HttpClientHelper.getToken() == null){
            respErrorCallback.onRespErrorCallback("Token mangler!");
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/user")
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

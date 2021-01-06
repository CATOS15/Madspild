package madspild.HttpClient;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Objects;

import madspild.Helpers.HttpClientHelper;
import madspild.Models.Family;
import madspild.Models.Inventory;

public class FamilyClient extends HttpClient {
    public void getUserFamily(RespCallback respCallback, RespErrorCallback respErrorCallback){
        if(HttpClientHelper.getToken() == null){
            respErrorCallback.onRespErrorCallback("Token mangler!");
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/family")
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
                    respCallback.onRespCallback(mapper.readValue(responseBody, Family.class));
                }else{
                    respErrorCallback.onRespErrorCallback(responseBody);
                }
            }
        });
    }
}

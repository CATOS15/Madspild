package madspild.HttpClient;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import madspild.Helpers.HttpClientHelper;
import madspild.Models.Inventory;
import madspild.Models.User;

public class InventoryClient extends HttpClient {
    public void createInventory(Inventory inventory, RespCallback respCallback, RespErrorCallback respErrorCallback){
        try {
            if(HttpClientHelper.getToken() == null){
                respErrorCallback.onRespErrorCallback("Token mangler!");
            }

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(inventory));
            Request request = new Request.Builder()
                    .url(BASE_URL + "/inventory")
                    .header("Authorization", "Bearer " + HttpClientHelper.getToken())
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
                        respCallback.onRespCallback(mapper.readValue(responseBody, Inventory.class));
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
    public void updateInventory(Inventory inventory, RespCallback respCallback, RespErrorCallback respErrorCallback){
        try {
            if(HttpClientHelper.getToken() == null){
                respErrorCallback.onRespErrorCallback("Token mangler!");
            }

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(inventory));
            Request request = new Request.Builder()
                    .url(BASE_URL + "/inventory")
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
                        respCallback.onRespCallback(mapper.readValue(responseBody, Inventory.class));
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

    public void deleteInventory(List<UUID> ids, RespCallback respCallback, RespErrorCallback respErrorCallback){
        if(HttpClientHelper.getToken() == null){
            respErrorCallback.onRespErrorCallback("Token mangler!");
        }

        OkHttpClient client = new OkHttpClient();

        StringBuilder idsString = new StringBuilder();
        for(int i = 0;i<ids.size();i++){
            if(i==0) {
                idsString.append("?ids=").append(ids.toString());
            }else{
                idsString.append("&ids=").append(ids.toString());
            }
        }

        Request request = new Request.Builder()
                .url(BASE_URL + "/inventory" + idsString.toString())
                .header("Authorization", "Bearer " + HttpClientHelper.getToken())
                .delete()
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
                    respCallback.onRespCallback(mapper.readValue(responseBody, String.class));
                }else{
                    respErrorCallback.onRespErrorCallback(responseBody);
                }
            }
        });
    }
}

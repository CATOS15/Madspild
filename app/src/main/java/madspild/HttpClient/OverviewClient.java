package madspild.HttpClient;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.Config.HttpClient;
import madspild.Models.Overview;

public class OverviewClient extends HttpClient {
    public void getUserOverview(Boolean deleted, RespCallback respCallback, RespErrorCallback respErrorCallback){
        if(HttpClientHelper.getToken() == null){
            respErrorCallback.onRespErrorCallback("Token mangler!");
        }

        //deleted = null    ALLE
        //deleted = true    KUN DEM DER ER SLETTET
        //deleted = false   KUN DEM DER IKKE ER SLETTET
        String deletedParam = "";
        if(deleted != null){
            deletedParam = "?deleted=" + (deleted ? 1 : 0);
        }
        Request request = new Request.Builder()
                .url(BASE_URL + "/overview" + deletedParam)
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
                    CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Overview.class);
                    respCallback.onRespCallback(mapper.readValue(responseBody, collectionType));
                }else{
                    respErrorCallback.onRespErrorCallback(responseBody);
                }
            }
        });
    }
}

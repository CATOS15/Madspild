package madspild.HttpClient.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

public abstract class HttpClient {
    protected final String BASE_URL = "http://www.madspild.com/api";
    protected final ObjectMapper mapper;

    protected final OkHttpClient client;

    public interface RespCallback{
        void onRespCallback(Object respObject);
    }

    public interface RespErrorCallback{
        void onRespErrorCallback(String respError);
    }

    public HttpClient(){
        mapper = new ObjectMapper();
        client = new OkHttpClient();
        client.interceptors().add(new HttpInterceptor());
    }
}

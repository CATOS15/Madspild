package madspild.HttpClient.Config;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import madspild.Helpers.HttpClientHelper;

import static com.squareup.okhttp.internal.Internal.logger;
import static java.lang.Thread.sleep;

public class HttpInterceptor implements Interceptor {
    boolean showLoading = true;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        showLoading = true;
        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        if(showLoading) HttpClientHelper.setLoading(true);
                    }
                },500);

        Response response = chain.proceed(request);
        showLoading = false;
        HttpClientHelper.setLoading(false);

        return response;
    }
}

package madspild.HttpClient.Config;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import madspild.Helpers.HttpClientHelper;

import static com.squareup.okhttp.internal.Internal.logger;
import static java.lang.Thread.sleep;

public class HttpInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpClientHelper.setLoading(true);

        Response response = chain.proceed(request);
        HttpClientHelper.setLoading(false);

        return response;
    }
}

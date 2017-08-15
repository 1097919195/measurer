package stuido.tsing.iclother.utils.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;
import stuido.tsing.iclother.utils.ApiException;

/**
 * Created by Endless on 2017/7/19.
 */

public class HttpHelper {
    private static final String BASE_URL = "http://liutsing.io/api/";
    private static final int DEFAULT_TIMEOUT = 600;
    protected Retrofit retrofit;

    //构造方法私有
    protected HttpHelper() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public class HttpResponseFunc<T> implements Func1<HttpResponse<T>, T> {
        @Override
        public T call(HttpResponse<T> httpResponse) {
            if (httpResponse.getStatus() >= 1000)
                throw new ApiException(httpResponse.getMsg());
//            if (httpResponse.getData() == null) throw new ApiException("暂无数据");
            return httpResponse.getData();
        }
    }
}
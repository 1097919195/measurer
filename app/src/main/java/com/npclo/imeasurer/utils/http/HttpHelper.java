package com.npclo.imeasurer.utils.http;

import android.text.TextUtils;

import com.blankj.utilcode.util.CacheUtils;
import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.utils.ApiException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;

/**
 * Created by Endless on 2017/7/19.
 */

public class HttpHelper {
    private static final String BASE_URL = "http://www.npclo.com/api/";
    private static final int DEFAULT_TIMEOUT = 600;
    protected Retrofit retrofit;
    private String token = "npclo_tS_7zerat";

    //构造方法私有
    protected HttpHelper() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            Date date = new Date();
            long time = date.getTime();
            String uid = CacheUtils.getInstance().getString("uid");
            if (TextUtils.isEmpty(uid)) uid = "";
            String jwt = Jwts.builder()
                    .setIssuer("http://www.npclo.com")
                    .setExpiration(new Date(time + 600))
                    .signWith(SignatureAlgorithm.HS256, token)
                    .setSubject(uid)
                    .compact();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("X-Authorization", jwt); // <-- this is the important line
            Request request = requestBuilder.build();
            return chain.proceed(request);

        });
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public class HttpResponseFunc<T> implements Func1<HttpResponse<T>, T> {
        @Override
        public T call(HttpResponse<T> httpResponse) {
            if (httpResponse.getStatus() >= 1000)
                throw new ApiException(httpResponse.getMsg());
            if (httpResponse.getData() == null) throw new ApiException("暂无数据");
            return httpResponse.getData();
        }
    }
}
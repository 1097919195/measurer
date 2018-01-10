package com.npclo.imeasurer.utils.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.npclo.imeasurer.R;
import com.npclo.imeasurer.base.BaseApplication;
import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.utils.Constant;
import com.npclo.imeasurer.utils.exception.ApiException;
import com.npclo.imeasurer.utils.exception.TimeoutException;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;

/**
 * @author Endless
 * @date 2017/7/19
 */

public class HttpHelper {
    private static final int DEFAULT_TIMEOUT = 6000;
    private static final int TIMEOUT_STATUS = 1430;
    private static final int EXCEPTION_THRESHOLD = 1000;
    protected Retrofit retrofit;

    protected HttpHelper() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        initHeader(httpClientBuilder);
        String httpScheme = Constant.getHttpScheme();
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(httpScheme + Constant.API_BASE_URL)
                .build();
    }

    private void initHeader(OkHttpClient.Builder httpClientBuilder) {
        Context context = BaseApplication.AppContext;
        SharedPreferences preferences = context.getSharedPreferences(
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        String jwt = preferences.getString("token", "thisisadefaultjwt");
        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("X-Authorization", jwt);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
    }

    public class HttpResponseFunc<T> implements Func1<HttpResponse<T>, T> {
        @Override
        public T call(HttpResponse<T> httpResponse) {
            //att 显示错误信息
            int status = httpResponse.getStatus();
            if (status >= EXCEPTION_THRESHOLD) {
                if (status == TIMEOUT_STATUS) {
                    throw new TimeoutException(httpResponse.getMsg());
                } else {
                    throw new ApiException(httpResponse.getMsg());
                }
            }
            if (httpResponse.getData() == null) {
                throw new ApiException("暂无数据");
            }
            return httpResponse.getData();
        }
    }
}
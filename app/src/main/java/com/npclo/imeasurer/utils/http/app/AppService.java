package com.npclo.imeasurer.utils.http.app;

import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.data.app.App;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Endless on 2017/7/19.
 */

public interface AppService {
    @GET("client/info")
    Observable<HttpResponse<App>> getLatestVersion();
}
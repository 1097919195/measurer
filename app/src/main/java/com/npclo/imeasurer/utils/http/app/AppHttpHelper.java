package com.npclo.imeasurer.utils.http.app;

import com.npclo.imeasurer.data.app.App;
import com.npclo.imeasurer.utils.http.HttpHelper;

import rx.Observable;

public class AppHttpHelper extends HttpHelper {
    public Observable<App> getLatestVersion() {
        return retrofit.create(AppService.class)
                .getLatestVersion()
                .map(new HttpResponseFunc<>());
    }
}

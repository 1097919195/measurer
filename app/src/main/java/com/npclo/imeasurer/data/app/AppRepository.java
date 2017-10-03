package com.npclo.imeasurer.data.app;

import com.npclo.imeasurer.utils.http.app.AppHttpHelper;

import rx.Observable;

/**
 * Created by Endless on 2017/9/19.
 */

public class AppRepository implements AppDataSource {

    public Observable<App> getLatestVersion() {
        return new AppHttpHelper().getLatestVersion();
    }
}

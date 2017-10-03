package com.npclo.imeasurer.data.app;

import rx.Observable;

/**
 * Created by Endless on 2017/9/19.
 */

public interface AppDataSource {
    Observable<App> getLatestVersion();
}

package com.npclo.imeasurer.base;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Endless on 2017/7/19.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: 2017/8/2 判断当前手机API版本
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            // TODO: 2017/8/28 heap analysis
            return;
        }
        LeakCanary.install(this);
    }
}

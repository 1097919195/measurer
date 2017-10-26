package com.npclo.imeasurer.base;

import android.app.Application;
import android.content.Context;

import com.npclo.imeasurer.utils.CrashHandler;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.internal.RxBleLog;
import com.squareup.leakcanary.LeakCanary;

import java.util.UUID;

/**
 *
 * @author Endless
 * @date 2017/7/19
 */

public class BaseApplication extends Application {
    private RxBleClient rxBleClient;
    private UUID characteristicUUID;
    private boolean haveUpdate = false;
    private boolean isFirstCheckUpdate = true;
    private String macAddress;

    public static RxBleClient getRxBleClient(Context context) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        return application.rxBleClient;
    }

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
        rxBleClient = RxBleClient.create(this);
        RxBleClient.setLogLevel(RxBleLog.DEBUG);
        //att 处理app crash
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public static void setNotificationUUID(Context context, UUID characteristicUUID) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        application.characteristicUUID = characteristicUUID;
    }

    public static UUID getUUID(Context context) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        return application.characteristicUUID;
    }

    /*=================TODO  elegant handle this=====================**/
    public static void haveUpdate(Context context, boolean b) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        application.haveUpdate = b;
    }

    public static boolean canUpdate(Context context) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        return application.haveUpdate;
    }

    public static void setIsFirstCheck(Context context) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        application.isFirstCheckUpdate = false;
    }

    public static boolean getFirstCheckHint(Context context) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        return application.isFirstCheckUpdate;
    }

    public static void setBleAddress(Context context, String macAddress) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        application.macAddress = macAddress;
    }

    public static String getMacAddress(Context context) {
        BaseApplication application = ((BaseApplication) context.getApplicationContext());
        return application.macAddress;
    }
}

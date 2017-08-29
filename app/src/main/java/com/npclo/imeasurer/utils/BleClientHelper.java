package com.npclo.imeasurer.utils;

import android.content.Context;

import com.polidea.rxandroidble.RxBleClient;

/**
 * Created by Endless on 2017/8/2.
 */

public class BleClientHelper {
    public static RxBleClient INSTANCE;

    public static RxBleClient getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = RxBleClient.create(context);
        }
        return INSTANCE;
    }
}

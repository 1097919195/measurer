package com.npclo.imeasurer.utils;

import android.util.Log;

/**
 * 自定义调试工具，方便筛选debug信息
 * Created by Endless on 2017/10/22.
 */

public class Gog {
    private static final String TAG = "GTech";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}

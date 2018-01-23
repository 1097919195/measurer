package com.npclo.imeasurer.utils;

import android.content.Context;

import com.google.common.base.Splitter;
import com.npclo.imeasurer.utils.http.app.AppHttpHelper;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import java.io.File;
import java.util.Map;


/**
 * Created by Endless on 2017/9/22.
 */

public class LogUtils {
    public static void upload(Context context) {
        File dir = new File(context.getExternalFilesDir("log") + "");
        String EXTENSION = "trace";
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (file.getPath().substring(file.getPath().length() - EXTENSION.length()).equals(EXTENSION)) {
                    upload(file);
                }
            }
        }
    }

    private static void clear(File file) {
        try {
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void upload(File file) {
        new AppHttpHelper().upload(file)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .subscribe(msg -> clear(file), e -> {
                });
    }

    public static void fixBug(String s) {
        new AppHttpHelper().fixbug(s)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .subscribe();
    }

    public static String getStackMsg(Exception e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    public static String getStackMsg(Throwable e) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (StackTraceElement el : stackArray) {
            sb.append(el.toString() + "\n");
        }
        return sb.toString();
    }

    public static String getParams(String url, String name) {
        String params = url.substring(url.indexOf("?") + 1, url.length());
        Map<String, String> split = Splitter.on("&").withKeyValueSeparator("=").split(params);
        return split.get(name);
    }
}

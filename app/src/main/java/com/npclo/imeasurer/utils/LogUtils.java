package com.npclo.imeasurer.utils;

import android.content.Context;

import com.npclo.imeasurer.utils.http.app.AppHttpHelper;
import com.npclo.imeasurer.utils.schedulers.SchedulerProvider;

import java.io.File;


/**
 * Created by Endless on 2017/9/22.
 */

public class LogUtils {
    private static final String TAG = LogUtils.class.getSimpleName();

    public static void upload(Context context) {
        File dir = new File(context.getExternalFilesDir("log") + "");
        String EXTENSION = "trace";
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (file.getPath().substring(file.getPath().length() - EXTENSION.length()).equals(EXTENSION))
                    upload(file);
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
                .subscribeOn(SchedulerProvider.getInstance().computation())
                .subscribe(msg -> clear(file), e -> {
                });
    }

}

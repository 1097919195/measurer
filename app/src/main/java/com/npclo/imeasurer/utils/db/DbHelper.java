package com.npclo.imeasurer.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.npclo.imeasurer.data.measure.local.MeasurementDbHelper;

/**
 * Created by Endless on 2017/7/31.
 */

public abstract class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private String SQL_CREATE_ENTRIES = createEntries();

    @NonNull
    protected abstract String createEntries();

    public DbHelper(Context context) {
        super(context, MeasurementDbHelper.DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}

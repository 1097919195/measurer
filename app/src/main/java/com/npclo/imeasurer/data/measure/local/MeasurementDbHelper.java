package com.npclo.imeasurer.data.measure.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.npclo.imeasurer.utils.db.MeasurementsPersistenceContract;
import com.npclo.imeasurer.utils.db.DbHelper;

/**
 * Created by Endless on 2017/7/31.
 */

public class MeasurementDbHelper extends DbHelper {
    public static final String DATABASE_NAME = "Measurement.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    public MeasurementDbHelper(Context context) {
        super(context);
    }

    @NonNull
    protected String createEntries() {
        return "CREATE TABLE " + MeasurementsPersistenceContract.MeasurementEntry.TABLE_NAME + " (" +
                MeasurementsPersistenceContract.MeasurementEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_DATA + TEXT_TYPE + COMMA_SEP +
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_GENDER + BOOLEAN_TYPE +
                " )";
    }
}

package stuido.tsing.iclother.data.measure.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;
import stuido.tsing.iclother.data.wuser.WeiXinUser;
import stuido.tsing.iclother.utils.db.MeasurementsPersistenceContract;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/7/31.
 */

public class MeasurementLocalDataSource implements MeasurementDataSource {
    private BriteDatabase briteDatabase;
    private static MeasurementLocalDataSource INSTANCE;
    @NonNull
    private Func1<Cursor, Measurement> mMapperFunction;

    private MeasurementLocalDataSource(@NonNull Context context, @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context);
        checkNotNull(schedulerProvider);
        MeasurementDbHelper measurementDbHelper = new MeasurementDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        briteDatabase = sqlBrite.wrapDatabaseHelper(measurementDbHelper, schedulerProvider.io());
        mMapperFunction = this::getMeasurement;
    }

    @NonNull
    private Measurement getMeasurement(@NonNull Cursor c) {
        String itemId = c.getString(c.getColumnIndexOrThrow(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_ENTRY_ID));
        String userId = c.getString(c.getColumnIndexOrThrow(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_USER_ID));
        String data =
                c.getString(c.getColumnIndexOrThrow(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_DATA));
        int gender = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_GENDER)));
        WeiXinUser weiXinUser = new WeiXinUser();
        weiXinUser.setSex(gender)
                .setWid(userId);
        return new Measurement(weiXinUser, data, itemId);
    }

    public static MeasurementLocalDataSource getInstance(@NonNull Context context, @NonNull BaseSchedulerProvider provider) {
        if (INSTANCE == null) {
            INSTANCE = new MeasurementLocalDataSource(context, provider);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Measurement>> getMeasurements() {
        String[] projection = {
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_ENTRY_ID,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_DATA,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_USER_ID,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_GENDER
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), MeasurementsPersistenceContract.MeasurementEntry.TABLE_NAME);
        return briteDatabase.createQuery(MeasurementsPersistenceContract.MeasurementEntry.TABLE_NAME, sql)
                .mapToList(mMapperFunction);
    }

    @Override
    public Observable<Measurement> getMeasurement(@NonNull String id) {
        String[] projection = {
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_ENTRY_ID,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_DATA,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_USER_ID,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_GENDER
        };
        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
                TextUtils.join(",", projection), MeasurementsPersistenceContract.MeasurementEntry.TABLE_NAME,
                MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_ENTRY_ID);
        return briteDatabase.createQuery(MeasurementsPersistenceContract.MeasurementEntry.TABLE_NAME, sql, id)
                .mapToOneOrDefault(mMapperFunction, null);
    }

    @Override
    public void saveMeasurement(@NonNull Measurement measurement) {
        checkNotNull(measurement);
        ContentValues values = new ContentValues();
        values.put(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_ENTRY_ID, measurement.getmId());
        values.put(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_DATA, measurement.getmData());
        values.put(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_USER_ID, measurement.getUser().getNickname());
        values.put(MeasurementsPersistenceContract.MeasurementEntry.COLUMN_NAME_GENDER, measurement.getUser().getSex());
        briteDatabase.insert(MeasurementsPersistenceContract.MeasurementEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void refreshMeasurements() {

    }
}

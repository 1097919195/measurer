package stuido.tsing.iclother.data.measure.remote;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;
import stuido.tsing.iclother.utils.http.measurement.MeasurementHelper;

/**
 * Created by Endless on 2017/7/31.
 */

public class MeasurementRemoteDataSource implements MeasurementDataSource {
    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private static MeasurementRemoteDataSource INSTANCE;

    public static MeasurementRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MeasurementRemoteDataSource();
        }
        return INSTANCE;
    }

    private MeasurementRemoteDataSource() {
    }

    @Override
    public Observable<List<Measurement>> getMeasurements() {
        return new MeasurementHelper().getMeasurements()
                .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Measurement> getMeasurement(@NonNull String id) {
        return new MeasurementHelper().getMeasurement(id)
                .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS);
    }

    @Override
    public void saveMeasurement(@NonNull Measurement measurement) {
        new MeasurementHelper().saveMeasurement(measurement);
    }

    @Override
    public void refreshMeasurements() {

    }
}

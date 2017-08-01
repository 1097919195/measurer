package stuido.tsing.iclother.data.measure.remote;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;
import stuido.tsing.iclother.utils.http.measurement.MeasurementHelper;

/**
 * Created by Endless on 2017/7/31.
 */

public class MeasurementRemoteDataSource implements MeasurementDataSource {
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
        return new MeasurementHelper().getMeasurements();
    }

    @Override
    public Observable<Measurement> getMeasurement(@NonNull String id) {
        return new MeasurementHelper().getMeasurement(id);
    }

    @Override
    public void saveMeasurement(@NonNull Measurement measurement) {
        new MeasurementHelper().saveMeasurement(measurement);
    }

    @Override
    public void refreshMeasurements() {

    }
}

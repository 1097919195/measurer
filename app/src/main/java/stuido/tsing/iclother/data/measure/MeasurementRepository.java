package stuido.tsing.iclother.data.measure;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * Created by Endless on 2017/7/31.
 */

public class MeasurementRepository implements MeasurementDataSource {
    @Override
    public Observable<List<Measurement>> getMeasurements() {
        return null;
    }

    @Override
    public Observable<Measurement> getMeasurement(@NonNull String id) {
        return null;
    }

    @Override
    public void saveMeasurement(@NonNull Measurement measurement) {

    }

    @Override
    public void refreshMeasurements() {

    }
}

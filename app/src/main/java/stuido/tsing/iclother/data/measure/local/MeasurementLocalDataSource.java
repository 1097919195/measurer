package stuido.tsing.iclother.data.measure.local;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.data.measure.MeasurementDataSource;

/**
 * Created by Endless on 2017/7/31.
 */

public class MeasurementLocalDataSource implements MeasurementDataSource {
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

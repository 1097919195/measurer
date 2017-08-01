package stuido.tsing.iclother.utils.http.measurement;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.utils.http.HttpHelper;
import stuido.tsing.iclother.utils.http.HttpResponse;

public class MeasurementHelper extends HttpHelper {
    public Observable<List<Measurement>> getMeasurements() {
        Observable<HttpResponse<List<Measurement>>> measurements = retrofit.create(MeasurementService.class).getMeasurements();

        return measurements.map(new HttpResponseFunc<>());
    }

    public Observable<Measurement> getMeasurement(@Path("id") String id) {
        Observable<HttpResponse<Measurement>> measurement = retrofit.create(MeasurementService.class).getMeasurement(id);
        return measurement.map(new HttpResponseFunc<>());
    }

    public Observable<HttpResponse> saveMeasurement(@Body Measurement measurement) {
        return retrofit.create(MeasurementService.class).saveMeasurement(measurement);
    }
}

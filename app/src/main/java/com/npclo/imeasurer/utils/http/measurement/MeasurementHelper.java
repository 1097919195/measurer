package com.npclo.imeasurer.utils.http.measurement;


import com.npclo.imeasurer.utils.http.HttpHelper;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Path;
import rx.Observable;

import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.utils.http.HttpResponse;

public class MeasurementHelper extends HttpHelper {
    public Observable<List<Measurement>> getMeasurements() {
        return retrofit.create(MeasurementService.class)
                .getMeasurements()
                .map(new HttpResponseFunc<>());
    }

    public Observable<Measurement> getMeasurement(@Path("id") String id) {
        Observable<HttpResponse<Measurement>> measurement = retrofit.create(MeasurementService.class).getMeasurement(id);
        return measurement.map(new HttpResponseFunc<>());
    }

    public Observable<HttpResponse> saveMeasurement(@Body String measurement) {
        return retrofit.create(MeasurementService.class).saveMeasurement(measurement);
    }
}

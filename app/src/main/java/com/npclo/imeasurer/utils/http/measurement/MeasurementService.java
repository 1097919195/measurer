package com.npclo.imeasurer.utils.http.measurement;

import com.npclo.imeasurer.data.measure.Measurement;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

import com.npclo.imeasurer.utils.http.HttpResponse;

/**
 * Created by Endless on 2017/8/1.
 */

public interface MeasurementService {
    @GET("measurement/list")
    Observable<HttpResponse<List<Measurement>>> getMeasurements();

    @GET("measurement/single/{id}")
    Observable<HttpResponse<Measurement>> getMeasurement(@Path("id") String id);

    @POST("measurement/save")
    Observable<HttpResponse> saveMeasurement(@Body String measurement);
}
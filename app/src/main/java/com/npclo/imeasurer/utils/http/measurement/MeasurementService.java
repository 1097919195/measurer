package com.npclo.imeasurer.utils.http.measurement;

import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.data.measure.Measurement;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Endless on 2017/8/1.
 */

public interface MeasurementService {
    @GET("measurement/list")
    Observable<HttpResponse<List<Measurement>>> getMeasurements();

    @GET("measurement/single/{id}")
    Observable<HttpResponse<Measurement>> getMeasurement(@Path("id") String id);

    @Multipart
    @POST("measurement/save")
    Observable<HttpResponse> saveMeasurement(
            @Part("measurement") String measurement, @Part("nonce") String randomStr,
            @Part("timeStamp") String timeStamp, @Part MultipartBody.Part[] imgs);
}

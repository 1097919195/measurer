package com.npclo.imeasurer.utils.http.measurement;

import com.npclo.imeasurer.data.HttpResponse;
import com.npclo.imeasurer.data.measure.Item;
import com.npclo.imeasurer.data.measure.Measurement;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * @author Endless
 * @date 2017/8/1
 */

public interface MeasurementService {
    @GET("measureparts/default")
    Observable<HttpResponse<List<Item>>> getDefaultMeasureParts(@Query("oid") String orgId);

    @GET("measureparts/contract")
    Observable<HttpResponse<List<Item>>> getMeasureParts(@Query("oid") String orgId, @Query("cid") String cid);

    @GET("measurement/single/{id}")
    Observable<HttpResponse<Measurement>> getMeasurement(@Path("id") String id);

    @Multipart
    @POST("measurement/save")
    Observable<HttpResponse> saveMeasurement(
            @Part("measurement") String measurement, @Part MultipartBody.Part[] imgs);
}

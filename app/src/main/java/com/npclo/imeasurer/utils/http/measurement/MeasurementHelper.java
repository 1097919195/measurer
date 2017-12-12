package com.npclo.imeasurer.utils.http.measurement;


import com.npclo.imeasurer.data.measure.Contract;
import com.npclo.imeasurer.data.measure.Item;
import com.npclo.imeasurer.data.measure.Result;
import com.npclo.imeasurer.utils.http.HttpHelper;

import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;

/**
 * @author Endless
 */
public class MeasurementHelper extends HttpHelper {
    public Observable<List<Item>> getDefaultMeasureParts(String oid) {
        return retrofit.create(MeasurementService.class)
                .getDefaultMeasureParts(oid)
                .map(new HttpResponseFunc<>());
    }

    public Observable<Contract> getContractInfoWithCode(String id) {
        return retrofit.create(MeasurementService.class)
                .getMeasureParts(id)
                .map(new HttpResponseFunc<>());
    }

    public Observable<Result> saveMeasurement(String measurement, MultipartBody.Part[] imgs) {
        return retrofit.create(MeasurementService.class)
                .saveMeasurement(measurement, imgs)
                .map(new HttpResponseFunc<>());
    }
}

package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;


public interface MeasurementDataSource {

    Observable<List<Measurement>> getMeasurements();

    Observable<Measurement> getMeasurement(@NonNull String id);

    void saveMeasurement(@NonNull Measurement measurement);

    void refreshMeasurements();
}

package com.npclo.imeasurer.main.measure;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.measure.Measurement;
import com.polidea.rxandroidble.RxBleConnection;

import java.util.UUID;

import rx.Observable;

/**
 * Created by Endless on 2017/9/1.
 */

public interface MeasureContract {
    interface View extends BaseView<Presenter> {

        void handleError(Throwable e);

        void showStartReceiveData();

        void bleDeviceMeasuring();

        void handleMeasureData(float v, float a2, int a3);

        void showSuccessSave();

        void showSaveError(Throwable e);

        void showSaveCompleted();

        void showLoading(boolean b);
    }

    interface Presenter extends BasePresenter {
        void startMeasure(UUID characteristicUUID, Observable<RxBleConnection> connectionObservable);

        void saveMeasurement(Measurement measurement);
    }
}

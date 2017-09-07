package com.npclo.imeasurer.main.measure;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
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

        void handleMeasureData(float v, int a2, int a3);
    }

    interface Presenter extends BasePresenter {
        void startMeasure(UUID characteristicUUID, Observable<RxBleConnection> connectionObservable);
    }
}

package com.npclo.imeasurer.main.measure;

import android.support.annotation.NonNull;

import com.npclo.imeasurer.utils.HexString;
import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;
import com.polidea.rxandroidble.RxBleConnection;

import java.util.UUID;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Endless on 2017/9/1.
 */

public class MeasurePresenter implements MeasureContract.Presenter {
    private static final String TAG = MeasurePresenter.class.getSimpleName();
    @NonNull
    private MeasureFragment fragment;
    @NonNull
    private BaseSchedulerProvider schedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;
//    private Observable<RxBleConnection> connectionObservable;
//    private UUID characteristicUUID;

    public MeasurePresenter(@NonNull MeasureContract.View view, @NonNull BaseSchedulerProvider schedulerProvider) {
        fragment = ((MeasureFragment) checkNotNull(view));
        this.schedulerProvider = checkNotNull(schedulerProvider);
        mSubscriptions = new CompositeSubscription();
        fragment.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void startMeasure(UUID characteristicUUID, Observable<RxBleConnection> connectionObservable) {
        connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
                .flatMap(notificationObservable -> notificationObservable)
                .doOnSubscribe(() -> fragment.bleDeviceMeasuring())
                .observeOn(schedulerProvider.ui())
                .doOnNext(notificationObservable -> fragment.showStartReceiveData())
                .subscribe(this::handleBleResult, this::handleError);
    }

    private void handleBleResult(byte[] v) {
        String s = HexString.bytesToHex(v);
        int code = Integer.parseInt("8D6A", 16);
        int length = Integer.parseInt(s.substring(0, 4), 16);
        int angle = Integer.parseInt(s.substring(4, 8), 16);
        int battery = Integer.parseInt(s.substring(8, 12), 16);
        int a1 = length ^ code;
        int a2 = angle ^ code;
        int a3 = battery ^ code;
        fragment.handleMeasureData((float) a1 / 10, a2, a3);
    }

    private void handleError(Throwable e) {
        fragment.handleError(e);
    }

//    @Override
//    public void saveMeasurement(Measurement measurement) {
//        String s = (new Gson()).toJson(measurement);
//        Log.e(TAG, s);
//        Subscription subscribe = new MeasurementHelper().saveMeasurement(s)
//                .subscribeOn(mSchedulerProvider.io())
//                .observeOn(mSchedulerProvider.ui())
//                .subscribe(__ -> measurementView.showSuccessSave(),
//                        e -> measurementView.showSaveError(),
//                        () -> measurementView.setLoadingIndicator(false)
//                );
//        mSubscription.add(subscribe);
//    }
}

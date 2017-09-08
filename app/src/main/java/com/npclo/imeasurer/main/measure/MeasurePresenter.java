package com.npclo.imeasurer.main.measure;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.utils.HexString;
import com.npclo.imeasurer.utils.http.measurement.MeasurementHelper;
import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;
import com.polidea.rxandroidble.RxBleConnection;

import java.util.UUID;

import rx.Observable;
import rx.Subscription;
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
        Log.e(TAG, s);
        int length = Integer.parseInt(s.substring(0, 4), 16);
        int angle = Integer.parseInt(s.substring(4, 8), 16);
        int battery = Integer.parseInt(s.substring(8, 12), 16);
        int a1 = length ^ code;
        int a2 = angle ^ code;
        int a3 = battery ^ code;
        Log.e(TAG, "length:" + length + "angle:" + a2 + "battery:" + a3);
        fragment.handleMeasureData((float) a1 / 10, (float) a2 / 10, a3);
    }

    private void handleError(Throwable e) {
        fragment.handleError(e);
    }

    @Override
    public void saveMeasurement(Measurement measurement) {
        String s = (new Gson()).toJson(measurement);
        Log.e(TAG, s);
        Subscription subscribe = new MeasurementHelper().saveMeasurement(s)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(__ -> fragment.showSuccessSave(),
                        e -> fragment.showSaveError(e),
                        () -> fragment.showSaveCompleted()
                );
        mSubscriptions.add(subscribe);
    }
}
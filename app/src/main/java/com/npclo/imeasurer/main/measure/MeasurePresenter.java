package com.npclo.imeasurer.main.measure;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.data.user.UserRepository;
import com.npclo.imeasurer.utils.HexString;
import com.npclo.imeasurer.utils.aes.AesException;
import com.npclo.imeasurer.utils.aes.AesUtils;
import com.npclo.imeasurer.utils.http.measurement.MeasurementHelper;
import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;
import com.polidea.rxandroidble.RxBleConnection;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MeasurePresenter implements MeasureContract.Presenter {
    private static final String TAG = MeasurePresenter.class.getSimpleName();
    @NonNull
    private MeasureFragment fragment;
    @NonNull
    private BaseSchedulerProvider schedulerProvider;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private AesUtils aesUtils;

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
        Subscription subscribe = connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
                .flatMap(notificationObservable -> notificationObservable)
                .doOnSubscribe(() -> fragment.bleDeviceMeasuring())
                .observeOn(schedulerProvider.ui())
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(this::handleBleResult, this::handleError);
        mSubscriptions.add(subscribe);
    }

    private void handleBleResult(byte[] v) {
        // FIXME: 2017/10/19 接收数据的间隔
        String s = HexString.bytesToHex(v);
        Log.e(TAG, "接收原始结果：" + s);
        if (s.length() == 16) { //判断接收到的数据是否准确
            int code = Integer.parseInt("8D6A", 16);
            int length = Integer.parseInt(s.substring(0, 4), 16);
            int angle = Integer.parseInt(s.substring(4, 8), 16);
            int battery = Integer.parseInt(s.substring(8, 12), 16);
            int a1 = length ^ code;
            int a2 = angle ^ code;
            int a3 = battery ^ code;
//            Log.e(TAG, "解析数据：长度: " + a1 + "; 角度:  " + a2 + "; 电量: " + a3);
            a1 += 14; //校正数据
            fragment.handleMeasureData((float) a1 / 10, (float) a2 / 10, a3);
        }
    }

    private void handleError(Throwable e) {
        fragment.handleError(e);
    }

    @Override
    public void saveMeasurement(Measurement measurement, MultipartBody.Part[] imgs) {
        String s = (new Gson()).toJson(measurement);
        if (aesUtils == null) aesUtils = new AesUtils();
        String s1 = null;
        String nonce = aesUtils.getRandomStr();
        String timeStamp = Long.toString(System.currentTimeMillis());
        try {
            s1 = aesUtils.encryptMsg(s, timeStamp, nonce);
        } catch (AesException e) {
            e.printStackTrace();
        }
        Subscription subscribe = new MeasurementHelper()
                .saveMeasurement(s1, imgs)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(__ -> fragment.showSuccessSave(),
                        e -> fragment.showSaveError(e),
                        () -> fragment.showSaveCompleted()
                );
        mSubscriptions.add(subscribe);
    }

    @Override
    public void getUserInfoWithOpenID(String id) {
        Subscription subscribe = new UserRepository()
                .getUserInfoWithOpenID(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(
                        user -> fragment.onGetWechatUserInfoSuccess(user),
                        e -> fragment.showGetInfoError(e),
                        () -> fragment.showCompleteGetInfo());
        mSubscriptions.add(subscribe);
    }

    @Override
    public void getUserInfoWithCode(String code) {
        Subscription subscribe = new UserRepository()
                .getUserInfoWithCode(code)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(
                        user -> fragment.onGetWechatUserInfoSuccess(user),
                        e -> fragment.showGetInfoError(e),
                        () -> fragment.showCompleteGetInfo());
        mSubscriptions.add(subscribe);
    }
}
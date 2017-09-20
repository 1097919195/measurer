package com.npclo.imeasurer.main.measure;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.utils.HexString;
import com.npclo.imeasurer.utils.aes.AesException;
import com.npclo.imeasurer.utils.aes.AesUtils;
import com.npclo.imeasurer.utils.http.measurement.MeasurementHelper;
import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;
import com.polidea.rxandroidble.RxBleConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.MultipartBody;
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
        Log.e(TAG, "==================所有观察者清空!!!!!!!!!!==============");
    }

    @Override
    public void startMeasure(UUID characteristicUUID, Observable<RxBleConnection> connectionObservable) {
        Subscription subscribe = connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
                .flatMap(notificationObservable -> notificationObservable)
                .doOnSubscribe(() -> fragment.bleDeviceMeasuring())
                .observeOn(schedulerProvider.ui())
                .doOnNext(notificationObservable -> fragment.showStartReceiveData())
                .subscribe(this::handleBleResult, this::handleError);
        mSubscriptions.add(subscribe);
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
//        Log.e(TAG, "测量原始结果："+s);
//        Log.e(TAG, "获得数据：长度: " +length + "; 角度:  " +angle+ "; 电量: " + battery);
        fragment.handleMeasureData((float) a1 / 10, (float) a2 / 10, a3);
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
            Log.e(TAG, "出错啦，" + e.getMessage());
            e.printStackTrace();
        }
        //检测
        check(s, s1, nonce, timeStamp);

        Subscription subscribe = new MeasurementHelper()
                .saveMeasurement(s1, nonce, timeStamp, imgs)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(() -> fragment.showLoading(true))
                .subscribe(__ -> fragment.showSuccessSave(),
                        e -> fragment.showSaveError(e),
                        () -> fragment.showSaveCompleted()
                );
        mSubscriptions.add(subscribe);
    }

    private void check(String s, String s1, String nonce, String timeStamp) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(s1);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
            NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

            String encrypt = nodelist1.item(0).getTextContent();
            String msgSignature = nodelist2.item(0).getTextContent();
            String fromXML = String.format("<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>"
                    , encrypt);

            String s2 = aesUtils.decryptMsg(msgSignature, timeStamp, nonce, fromXML);
            if (s.equals(s2)) Log.e(TAG, "正确解析");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AesException e) {
            e.printStackTrace();
        }
    }
}
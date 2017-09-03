//package com.npclo.imeasurer.measure;
//
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattService;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.npclo.imeasurer.data.measure.Measurement;
//import com.npclo.imeasurer.utils.HexString;
//import com.npclo.imeasurer.utils.http.measurement.MeasurementHelper;
//import com.npclo.imeasurer.utils.schedulers.BaseSchedulerProvider;
//import com.polidea.rxandroidble.RxBleClient;
//import com.polidea.rxandroidble.RxBleConnection;
//import com.polidea.rxandroidble.RxBleDevice;
//import com.polidea.rxandroidble.exceptions.BleAlreadyConnectedException;
//import com.polidea.rxandroidble.exceptions.BleScanException;
//import com.polidea.rxandroidble.scan.ScanFilter;
//import com.polidea.rxandroidble.scan.ScanSettings;
//import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;
//
//import java.util.UUID;
//
//import rx.Observable;
//import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.subjects.PublishSubject;
//import rx.subscriptions.CompositeSubscription;
//
//import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
//
//public class MeasurePresenter {
//    @NonNull
//    private RxBleClient rxBleClient;
//    @NonNull
//    private MeasureContract.View measurementView;
//    @NonNull
//    private CompositeSubscription mSubscription;
//    @NonNull
//    private Subscription scanSubscription;
//    private RxBleDevice bleDevice;
//    private UUID characteristicUUID;
//    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();
//    private Observable<RxBleConnection> connectionObservable;
//
//    public void subscribe() {
//
//    }
//
//    public void unsubscribe() {
//        mSubscription.clear();
//    }
//
//    public void saveMeasurement(Measurement measurement) {
//        String s = (new Gson()).toJson(measurement);
//        Log.e(getClass().toString() + "json:", s);
//        Subscription subscribe = new MeasurementHelper().saveMeasurement(s)
//                .subscribeOn(mSchedulerProvider.io())
//                .observeOn(mSchedulerProvider.ui())
//                .subscribe(__ -> measurementView.showSuccessSave(),
//                        e -> measurementView.showSaveError(),
//                        () -> measurementView.setLoadingIndicator(false)
//                );
//        mSubscription.add(subscribe);
//    }
//
//    /**
//     * 扫描按钮事件
//     */
//    public void scanToggle() {
//        if (isScanning()) {
//            scanSubscription.unsubscribe();
//        } else {
//            measurementView.setLoadingIndicator(true);
//            scanSubscription = rxBleClient.scanBleDevices(new ScanSettings.Builder()
//                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
//                            .build(),
//                    new ScanFilter.Builder().build())
//                    .doOnSubscribe(() -> measurementView.showScanning())
//                    .observeOn(mSchedulerProvider.ui())
//                    .doOnUnsubscribe(this::clearSubscription)
//                    .subscribe(scanResult -> {
//                        Log.e("扫描", "结果1");
//                        measurementView.handleScanResult(scanResult);
//                    }, this::handleError, () -> measurementView.finishScan());
//        }
//    }
//
//    private void clearSubscription() {
//        scanSubscription = null;
//        measurementView.updateButtonUIState();
//    }
//
//    public boolean isScanning() {
//        return scanSubscription != null;
//    }
//
//    private void connectDevice() {
//        if (isConnected()) {
//            triggerDisconnect();
//        } else {
//            connectionObservable
//                    .flatMap(RxBleConnection::discoverServices)
//                    .flatMap(rxBleDeviceServices -> rxBleDeviceServices.getCharacteristic(characteristicUUID))
//                    .observeOn(mSchedulerProvider.ui())
//                    .doOnUnsubscribe(() -> Log.e(getClass().toString(), "is on connecting"))
//                    .subscribe(c -> {
//                        measurementView.showConnected();
//                        Log.e(getClass().getSimpleName(), "Hey, connection has been established!");
//                    }, this::handleError);
//        }
//    }
//
//    /**
//     * 选择设备后，选择服务，再选择通知特性
//     *
//     * @param s
//     */
//    public void connectDevice(String s) {
//        bleDevice = rxBleClient.getBleDevice(s);
//        bleDevice.establishConnection(false)
//                .flatMap(RxBleConnection::discoverServices)
//                .first() // Disconnect automatically after discovery
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(deviceServices -> {
//                    for (BluetoothGattService service : deviceServices.getBluetoothGattServices()) {
//                        for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
//                            if (isCharacteristicNotifiable(characteristic)) {
//                                characteristicUUID = characteristic.getUuid();
//                                connectionObservable = prepareConnectionObservable();
//                                connectDevice();
//                                break;
//                            }
//                        }
//                    }
//                }, this::handleError);
//    }
//
//    public boolean isConnected() {
//        checkNotNull(bleDevice);
//        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
//    }
//
//    public void startMeasure() {
//        if (!isConnected()) {
//            measurementView.showBleDisconnectHint();
//            return;
//        }
//        connectionObservable
//                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
//                .flatMap(notificationObservable -> notificationObservable)
//                .doOnSubscribe(() -> measurementView.bleDeviceMeasuring())
//                .observeOn(mSchedulerProvider.ui())
//                .doOnNext(notificationObservable -> measurementView.showStartReceiveData())
//                .subscribe(this::handleBleResult, this::handleError);
//    }
//
//    private void handleBleResult(byte[] v) {
//        String s = HexString.bytesToHex(v);
//        int code = Integer.parseInt("8D6A", 16);
//        int length = Integer.parseInt(s.substring(0, 4), 16);
//        int angle = Integer.parseInt(s.substring(4, 8), 16);
//        int battery = Integer.parseInt(s.substring(8, 12), 16);
//        int a1 = length ^ code;
//        int a2 = angle ^ code;
//        int a3 = battery ^ code;
//        measurementView.updateMeasureData((float) a1 / 10, a2, a3);
//    }
//
//    private Observable<RxBleConnection> prepareConnectionObservable() {
//        checkNotNull(bleDevice);
//        return bleDevice
//                .establishConnection(false)
//                .takeUntil(disconnectTriggerSubject)
//                .compose(new ConnectionSharingAdapter());
//    }
//
//    private void handleError(Throwable e) {
//        if (e instanceof BleScanException) {
//            measurementView.handleBleScanException((BleScanException) e);
//        } else if (e instanceof BleAlreadyConnectedException) {
//            measurementView.showUnknownError("重复连接，请检查");
//        } else {
//            measurementView.showUnknownError();
//        }
//    }
//
//    private void triggerDisconnect() {
//        disconnectTriggerSubject.onNext(null);
//    }
//
//    private boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
//        return (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
//    }
//}
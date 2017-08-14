package stuido.tsing.iclother.measure;

import android.support.annotation.NonNull;
import android.util.Log;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleAlreadyConnectedException;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;

import java.util.UUID;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import stuido.tsing.iclother.data.measure.Measurement;
import stuido.tsing.iclother.utils.HexString;
import stuido.tsing.iclother.utils.http.measurement.MeasurementHelper;
import stuido.tsing.iclother.utils.schedulers.BaseSchedulerProvider;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class MeasurePresenter implements MeasureContract.Presenter {
    @NonNull
    private RxBleClient rxBleClient;
    @NonNull
    private MeasureContract.View measurementView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeSubscription mSubscription;
    @NonNull
    private Subscription scanSubscription;
    private RxBleDevice bleDevice;
    private UUID characteristicUUID;
    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();
    private Observable<RxBleConnection> connectionObservable;

    public MeasurePresenter(@NonNull RxBleClient client, @NonNull MeasureContract.View measure_view,
                            @NonNull BaseSchedulerProvider schedulerProvider) {
        rxBleClient = checkNotNull(client);
        measurementView = checkNotNull(measure_view);
        mSchedulerProvider = checkNotNull(schedulerProvider);
        measurementView.setPresenter(this);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscription.clear();
    }

    @Override
    public void saveMeasurement(Measurement measurement) {
        Subscription subscribe = new MeasurementHelper().saveMeasurement(measurement)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(__ -> measurementView.showSuccessSave(),
                        e -> measurementView.showSaveError(),
                        () -> measurementView.setLoadingIndicator(false)
                );
        mSubscription.add(subscribe);
    }

    /**
     * 扫描按钮事件
     */
    public void scanToggle() {
        if (isScanning()) {
            scanSubscription.unsubscribe();
        } else {
            measurementView.setLoadingIndicator(true);
            scanSubscription = rxBleClient.scanBleDevices(
                    new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                            .build(),
                    new ScanFilter.Builder().build())
                    .doOnSubscribe(() -> measurementView.showScanning())
                    .observeOn(mSchedulerProvider.ui())
                    .doOnUnsubscribe(this::clearSubscription)
                    .subscribe(scanResult -> {
                        measurementView.showScanResult(scanResult);
                        scanSubscription.unsubscribe();
                    }, this::handleError, () -> measurementView.finishScan());
//            scanSubscription.unsubscribe();
//            measurementView.updateButtonUIState();
        }
    }

    private void clearSubscription() {
        scanSubscription = null;
        measurementView.updateButtonUIState();
    }

    @Override
    public boolean isScanning() {
        return scanSubscription != null;
    }

    @Override
    public void connectDevice() {
        if (isConnected()) {
            triggerDisconnect();
        } else {
            connectionObservable
                    .flatMap(RxBleConnection::discoverServices)
                    .flatMap(rxBleDeviceServices -> rxBleDeviceServices.getCharacteristic(characteristicUUID))
                    .observeOn(mSchedulerProvider.ui())
                    .doOnUnsubscribe(() -> Log.e(getClass().toString(), "is on connecting"))
                    .subscribe(c -> {
                        measurementView.showConnected();
                        Log.e(getClass().getSimpleName(), "Hey, connection has been established!");
                    }, this::handleError);
        }
    }

    @Override
    public void discoveryServices(String s) {
        bleDevice = rxBleClient.getBleDevice(s);
        bleDevice.establishConnection(false)
                .flatMap(RxBleConnection::discoverServices)
                .first() // Disconnect automatically after discovery
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceServices -> measurementView.showServiceListView(deviceServices), this::handleError);
    }

    public boolean isConnected() {
        checkNotNull(bleDevice);
        return bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    @Override
    public void chooseCharacteristic(String uuid) {
        characteristicUUID = UUID.fromString(checkNotNull(uuid));
        connectionObservable = prepareConnectionObservable();
        connectDevice();
    }

    @Override
    public void startMeasure() {
        if (!isConnected()) {
            measurementView.showBleDisconnectHint();
            return;
        }
        connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
                .flatMap(notificationObservable -> notificationObservable)
                .doOnSubscribe(() -> measurementView.bleDeviceMeasuring())
                .observeOn(mSchedulerProvider.ui())
                .doOnNext(notificationObservable -> measurementView.showStartReceiveData())
                .subscribe(this::handleBleResult, this::handleError);
    }

    private void handleBleResult(byte[] v) {
        String s = HexString.bytesToHex(v);
        int code = Integer.parseInt("8D6A", 16);
        int length = Integer.parseInt(s.substring(0, 4), 16);
        int battery = Integer.parseInt(s.substring(4, 8), 16);
        int angle = Integer.parseInt(s.substring(8, 12), 16);
        int a1 = length ^ code;
        int a2 = battery ^ code;
        int a3 = angle ^ code;
        Log.e(getClass().toString(), "length:" + a1 + "mm;battery:" + a2 + ";angle:" + a3);
        measurementView.updateMeasureData(a1, a2, a3);
    }

    private Observable<RxBleConnection> prepareConnectionObservable() {
        checkNotNull(bleDevice);
        return bleDevice
                .establishConnection(false)
                .takeUntil(disconnectTriggerSubject)
                .compose(new ConnectionSharingAdapter());
    }

    private void handleError(Throwable e) {
        if (e instanceof BleScanException) {
            measurementView.handleBleScanException((BleScanException) e);
        } else if (e instanceof BleAlreadyConnectedException) {
            measurementView.showUnknownError("重复连接，请检查");
        } else {
            measurementView.showUnknownError();
        }
    }

    private void triggerDisconnect() {
        disconnectTriggerSubject.onNext(null);
    }
}
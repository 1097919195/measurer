package stuido.tsing.iclother.measure;

import android.bluetooth.BluetoothGattCharacteristic;

import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;

import stuido.tsing.iclother.base.BasePresenter;
import stuido.tsing.iclother.base.BaseView;
import stuido.tsing.iclother.data.measure.Measurement;

/**
 * Created by Endless on 2017/8/1.
 */

public interface MeasureContract {
    interface View extends BaseView<Presenter> {

        void showSuccessSave();

        void showSaveError();

        void setLoadingIndicator(boolean b);

        void handleBleScanException(BleScanException e);

        void showScanResult(ScanResult scanResult);

        void updateButtonUIState();

        void showServiceChoiceView(BluetoothGattCharacteristic characteristic);

        void showBleDisconnectHint();

        void updateMeasureData(int length, int battery, int angle);

        void showAlreadyConnectedError();

        void showConnecting();

        void showStartReceiveData();

        void showBleServicesDiscoveryView();

        void showUnknownError();

        void showServiceListView(RxBleDeviceServices deviceServices);

        void showUnknownError(String s);

        void finishScan();

        void showConnected();

        void bleDeviceMeasuring();

        void showScanning();
    }

    interface Presenter extends BasePresenter {
        void saveMeasurement(Measurement measurement);

        void scanToggle();

        boolean isScanning();

        void connectDevice();

        boolean isConnected();

        void startMeasure();

        void discoveryServices(String s);

        void chooseCharacteristic(String uuid);
    }
}

package stuido.tsing.iclother.measure;

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

        void handleScanResult(ScanResult scanResult);

        void updateButtonUIState();

        void showBleDisconnectHint();

        void updateMeasureData(float length, int battery, int angle);

//        void showAlreadyConnectedError();
//
//        void showConnecting();

        void showStartReceiveData();

        void showUnknownError();

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

        boolean isConnected();

        void startMeasure();

        void connectDevice(String s);
    }
}

package com.npclo.imeasurer.user.home;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;

/**
 * Created by Endless on 2017/8/24.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {

        void logout();

        void handleBleScanException(BleScanException e);

        void showError(String s);

        void showError();

        void showConnected();

        void isConnecting();

        void setLoadingIndicator(boolean bool);

        void showScanning();

        void handleScanResult(ScanResult scanResult);

        void closeScanResultDialog();
    }

    interface Presenter extends BasePresenter {
        void logout();

        void connectDevice(String s);

        void startScan();

    }
}

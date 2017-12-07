package com.npclo.imeasurer.main.home;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.app.App;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;

import java.util.UUID;

public interface HomeContract {
    interface Presenter extends BasePresenter {

        void getUserInfoWithCode(String result);

        void getUserInfoWithOpenID(String result);

        void getLatestVersion();

        void logout();

        void startScan();

        void connectDevice(String s);
    }

    interface View extends BaseView<Presenter> {

        void showLoading(boolean b);

        void onGetWechatUserInfoSuccess(WechatUser info);

        void showGetInfoError(Throwable e);

        void showCompleteGetInfo();

        void showGetVersionSuccess(App app);

        void showGetVersionError(Throwable e);

        void logout();

        void onHandleScanResult(ScanResult scanResult);

        void onShowError(String s);

        void onHandleError(Throwable e);

        void onHandleBleScanException(BleScanException e);

        void onSetNotificationUUID(UUID characteristicUUID);

        void onShowConnected(RxBleDevice bleDevice);

        void onCloseScanResultDialog();
    }
}
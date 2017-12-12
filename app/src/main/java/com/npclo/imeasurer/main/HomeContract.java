package com.npclo.imeasurer.main;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.app.App;
import com.npclo.imeasurer.data.measure.Contract;
import com.npclo.imeasurer.data.measure.Item;
import com.npclo.imeasurer.data.wuser.WechatUser;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;

import java.util.List;
import java.util.UUID;

public interface HomeContract {
    interface Presenter extends BasePresenter {

        void getUserInfoWithCode(String result, String uid);

        void getUserInfoWithOpenID(String result, String uid);

        void autoGetLatestVersion();

        void logout();

        void startScan();

        void connectDevice(String s);

        void manuallyGetLatestVersion();

        /**
         * 获取第三方组织的默认量体部位
         */
        void getThirdOrgDefaultParts(String oid);

        /**
         * 根据合同号获取第三方组织的量体部位
         */
        void getThirdOrgMeasurePartByContractNum();

        void getContractInfoWithCode(String result);

        void getContractInfoWithNum(String result);
    }

    interface View extends BaseView<Presenter> {

        void showLoading(boolean b);

        void onGetWechatUserInfo(WechatUser info);

        void showGetInfoError(Throwable e);

        void showCompleteGetInfo();

        void onGetVersionInfo(App app);

        void onGetVersionError(Throwable e);

        void onLogout();

        void onHandleScanResult(ScanResult scanResult);

        void onShowError(String s);

        void onHandleError(Throwable e);

        void onHandleBleScanException(BleScanException e);

        void onSetNotificationUUID(UUID characteristicUUID);

        void onDeviceChoose(RxBleDevice bleDevice);

        void onCloseScanResultDialog();

        void onDefaultMeasureParts(List<Item> partList);

        void startScanContractNum();

        void onHandleContractInfo(Contract contract);
    }
}
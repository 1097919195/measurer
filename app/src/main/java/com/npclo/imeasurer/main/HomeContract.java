package com.npclo.imeasurer.main;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.App;
import com.npclo.imeasurer.data.ThirdMember;
import com.npclo.imeasurer.data.measure.Contract;
import com.npclo.imeasurer.data.measure.Item;
import com.npclo.imeasurer.data.WechatUser;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;

import java.util.List;
import java.util.UUID;

public interface HomeContract {
    interface Presenter extends BasePresenter {

        void getUserInfoWithCode(String result);

        void getUserInfoWithOpenID(String result);

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

        void getThirdMemberInfo(String tid, String cid);
    }

    interface View extends BaseView<Presenter> {

        void showLoading(boolean b);

        void onGetWechatUserInfo(WechatUser info);

        void showGetInfoError(Throwable e);

        void showCompleteGetInfo();

        void onGetVersionInfo(App app, String type);

        void onGetVersionError(Throwable e, String type);

        void onLogout();

        void onHandleScanResult(ScanResult scanResult);

        void onShowError(String s);

        void onHandleBleScanException(BleScanException e);

        void onSetNotificationUUID(UUID characteristicUUID);

        void onDeviceChoose(RxBleDevice bleDevice);

        void onCloseScanResultDialog();

        void onDefaultMeasureParts(List<Item> partList);

        void startScanContractNum();

        void onHandleContractInfo(Contract contract);

        void onGetAngleOfParts(List<Item> list);

        void onGetAngleOfPartsError(Throwable e);

        void onUpdateUserInfoError(Throwable e);

        void onHandleUnknownError(Throwable e);

        void onHandleConnectError(Throwable e);

        void onGetThirdMemberInfo(ThirdMember member);

        void showGetThirdMemberInfoError(Throwable e);

        void showCompleteGetThirdMemberInfo();
    }
}
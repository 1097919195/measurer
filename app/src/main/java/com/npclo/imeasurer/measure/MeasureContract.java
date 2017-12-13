package com.npclo.imeasurer.measure;

import com.npclo.imeasurer.base.BasePresenter;
import com.npclo.imeasurer.base.BaseView;
import com.npclo.imeasurer.data.measure.Measurement;
import com.npclo.imeasurer.data.measure.Result;
import com.npclo.imeasurer.data.wuser.WechatUser;

import java.util.UUID;

import okhttp3.MultipartBody;

/**
 * Created by Endless on 2017/9/1.
 */

public interface MeasureContract {
    interface View extends BaseView<Presenter> {

        void handleMeasureData(float v, float a2, int a3);

        void onSaveSuccess(Result result);

        void showSaveError(Throwable e);

        void showSaveCompleted();

        void showLoading(boolean b);

        void onGetWechatUserInfoSuccess(WechatUser user);

        void showGetInfoError(Throwable e);

        void showCompleteGetInfo();

        void onShowDevicePrepareConnectionError();

        void onHandleMeasureError();

        void onHandleMeasureError(Throwable e);
    }

    interface Presenter extends BasePresenter {
        void saveMeasurement(Measurement measurement, MultipartBody.Part[] imgs);

        void getUserInfoWithOpenID(String id, String uid);

        void getUserInfoWithCode(String code, String uid);

        void reConnect();

        void setUUID(UUID characteristicUUID);
    }
}
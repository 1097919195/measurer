package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.wuser.WechatUser;

public class Measurement {
    public Measurement(@Nullable WechatUser wechatUser, @Nullable MeasurementItem data,
                       @NonNull String id) {
        _id = id;
        mData = data;
        user = wechatUser;
    }

    public Measurement(@Nullable WechatUser user, @Nullable MeasurementItem data) {
        this(user, data, "");
    }

    @Nullable
    private String _id;
    private MeasurementItem mData;
    private WechatUser user;

    @Nullable
    public String get_id() {
        return _id;
    }


    public void set_id(@Nullable String _id) {
        this._id = _id;
    }

    public MeasurementItem getmData() {
        return mData;
    }

    public void setmData(MeasurementItem data) {
        this.mData = data;
    }

    public WechatUser getUser() {
        return user;
    }

    public void setUser(WechatUser user) {
        this.user = user;
    }
}
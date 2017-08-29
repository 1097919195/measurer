package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.wuser.WeiXinUser;

public class Measurement {
    public Measurement(@Nullable WeiXinUser weiXinUser, @Nullable MeasurementItem data,
                       @NonNull String id) {
        _id = id;
        mData = data;
        user = weiXinUser;
    }

    public Measurement(@Nullable WeiXinUser user, @Nullable MeasurementItem data) {
        this(user, data, "");
    }

    @Nullable
    private String _id;
    private MeasurementItem mData;
    private WeiXinUser user;

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

    public WeiXinUser getUser() {
        return user;
    }

    public void setUser(WeiXinUser user) {
        this.user = user;
    }
}
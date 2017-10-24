package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.data.measure.item.MeasurementItem;
import com.npclo.imeasurer.data.wuser.WechatUser;

public class Measurement {
    public Measurement(@NonNull WechatUser wechatUser, @NonNull MeasurementItem data, @NonNull String userid,
                       @Nullable String id, @Nullable String oid) {
        _id = id;
        mData = data;
        uid = userid;
        user = wechatUser;
        orgId = oid;
    }

    public Measurement(@NonNull WechatUser user, @NonNull MeasurementItem data, @NonNull String userid, @Nullable String oid) {
        this(user, data, userid, "", oid);
    }

    @Nullable
    private String _id;
    private MeasurementItem mData;
    private WechatUser user;
    private String uid;
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
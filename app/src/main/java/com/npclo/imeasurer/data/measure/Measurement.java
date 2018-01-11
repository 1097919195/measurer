package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.data.WechatUser;

import java.util.List;

/**
 * @author Endless
 */
public class Measurement {
    public Measurement(@NonNull WechatUser wechatUser, @NonNull List<Part> data, @Nullable String cid) {
        this.data = data;
        user = wechatUser;
        this.cid = cid;
    }

    @Nullable
    private String id;
    private List<Part> data;
    private WechatUser user;
    private String uid;
    private String orgId;
    private String cid;

    public WechatUser getUser() {
        return user;
    }

    public void setUser(WechatUser user) {
        this.user = user;
    }
}
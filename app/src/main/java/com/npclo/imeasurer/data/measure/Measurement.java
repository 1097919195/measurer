package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.data.wuser.WechatUser;

import java.util.List;

/**
 * @author Endless
 */
public class Measurement {
    /**
     * @param wechatUser 待量体用户
     * @param data       量体数据
     * @param userid     量体师id
     * @param oid        量体师组织id
     * @param cid        本次量体的合同id
     */
    public Measurement(@NonNull WechatUser wechatUser, @NonNull List<Part> data, @NonNull String userid,
                       @Nullable String oid, @Nullable String cid) {
        orgId = oid;
        this.data = data;
        uid = userid;
        user = wechatUser;
        orgId = oid;
        this.cid = cid;
    }

    @Nullable
    private String _id;
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
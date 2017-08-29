package com.npclo.imeasurer.data.wuser;

import android.support.annotation.Nullable;

/**
 * Created by Endless on 2017/8/14.
 */

public class WeiXinUser {
    private int sex;
    private String nickname;
    @Nullable
    private String height;
    @Nullable
    private String weight;
    @Nullable
    private String openId;

    public int getSex() {
        return sex;
    }

    public WeiXinUser setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public WeiXinUser setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    @Nullable
    public String getHeight() {
        return height;
    }

    public WeiXinUser setHeight(@Nullable String height) {
        this.height = height;
        return this;
    }

    @Nullable
    public String getWeight() {
        return weight;
    }

    public WeiXinUser setWeight(@Nullable String weight) {
        this.weight = weight;
        return this;
    }

    @Nullable
    public String getOpenId() {
        return openId;
    }

    public WeiXinUser setOpenId(@Nullable String openId) {
        this.openId = openId;
        return this;
    }
}

package com.npclo.imeasurer.data.wuser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Endless on 2017/8/14.
 */

public class WechatUser {
    private int sex;
    private String nickname;
    @NonNull
    private String height;
    @NonNull
    private String weight;
    @NonNull
    private String openID;
    @Nullable
    private String avatar;
    @NonNull
    private String name;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public WechatUser setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public WechatUser setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    @Nullable
    public String getHeight() {
        return height;
    }

    public WechatUser setHeight(@Nullable String height) {
        this.height = height;
        return this;
    }

    @Nullable
    public String getWeight() {
        return weight;
    }

    public WechatUser setWeight(@Nullable String weight) {
        this.weight = weight;
        return this;
    }

    @Nullable
    public String getOpenID() {
        return openID;
    }

    public WechatUser setOpenID(@Nullable String openID) {
        this.openID = openID;
        return this;
    }
}

package com.npclo.imeasurer.data.user;

import android.support.annotation.Nullable;

/**
 * Created by Endless on 2017/7/19.
 */

public class User {
    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public String getPwd() {
        return pwd;
    }

    public User setPwd(@Nullable String pwd) {
        this.pwd = pwd;
        return this;
    }

    @Nullable
    public String get_id() {
        return _id;
    }

    public void set_id(@Nullable String _id) {
        this._id = _id;
    }

    private String name;
    private
    @Nullable
    String pwd;
    private
    @Nullable
    String _id;
}

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
    @Nullable
    private String pwd;
    @Nullable
    private String _id;
    @Nullable
    private int currTimes;
    @Nullable
    private int totalTimes;

    @Nullable
    public int getCurrTimes() {
        return currTimes;
    }

    public void setCurrTimes(@Nullable int currTimes) {
        this.currTimes = currTimes;
    }

    @Nullable
    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(@Nullable int totalTimes) {
        this.totalTimes = totalTimes;
    }
}

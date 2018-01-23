package com.npclo.imeasurer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Endless on 23/01/2018.
 */

public class ThirdMember implements Parcelable, IUser {
    private String name;
    private int gender = 1;
    private int times = 0;
    private String tid;

    private ThirdMember(Parcel in) {
        name = in.readString();
        gender = in.readInt();
        times = in.readInt();
        tid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(gender);
        dest.writeInt(times);
        dest.writeString(tid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThirdMember> CREATOR = new Creator<ThirdMember>() {
        @Override
        public ThirdMember createFromParcel(Parcel in) {
            return new ThirdMember(in);
        }

        @Override
        public ThirdMember[] newArray(int size) {
            return new ThirdMember[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}

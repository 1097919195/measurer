package stuido.tsing.iclother.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

public class Measurement {
    public Measurement(@Nullable String userId, @Nullable String data,
                       @NonNull String id, int sex) {
        mId = id;
        mData = data;
        mUserName = userId;
        mSex = sex;
    }

    public Measurement(@Nullable String userId, @Nullable String data, int sex) {
        this(userId, data, UUID.randomUUID().toString(), sex);
    }

    public String getmId() {
        return mId;
    }

    public String getmData() {
        return mData;
    }

    public String getmUserName() {
        return mUserName;
    }

    public int getmSex() {
        return mSex;
    }

    private String mId;
    private String mData;
    private String mUserName;
    private int mSex;
}

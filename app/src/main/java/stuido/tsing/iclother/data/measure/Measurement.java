package stuido.tsing.iclother.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

public class Measurement {
    public Measurement(@Nullable String userId, @Nullable String data,
                       @NonNull String id) {
        mId = id;
        mData = data;
        mUserId = userId;
    }

    public Measurement(@Nullable String userId, @Nullable String data) {
        this(userId, data, UUID.randomUUID().toString());
    }

    public String getmId() {
        return mId;
    }

    public String getmData() {
        return mData;
    }

    public String getmUserId() {
        return mUserId;
    }

    private String mId;
    private String mData;
    private String mUserId;
}

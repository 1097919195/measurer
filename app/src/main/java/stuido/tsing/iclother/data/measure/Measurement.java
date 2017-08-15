package stuido.tsing.iclother.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

import stuido.tsing.iclother.data.measure.item.MeasurementItem;
import stuido.tsing.iclother.data.wuser.WeiXinUser;

public class Measurement {
    public Measurement(@Nullable WeiXinUser weiXinUser, @Nullable MeasurementItem data,
                       @NonNull String id) {
        cId = id;
        mData = data;
        user = weiXinUser;
    }

    public Measurement(@Nullable WeiXinUser user, @Nullable MeasurementItem data) {
        this(user, data, UUID.randomUUID().toString());
    }

    private String cId;
    private MeasurementItem mData;
    private WeiXinUser user;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public MeasurementItem getmData() {
        return mData;
    }

    public void setmData(MeasurementItem data) {
        this.mData = data;
    }

    public WeiXinUser getUser() {
        return user;
    }

    public void setUser(WeiXinUser user) {
        this.user = user;
    }
}
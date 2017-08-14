package stuido.tsing.iclother.data.wuser;

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
    private String wid;

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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeight() {
        return height;
    }

    public WeiXinUser setHeight(String height) {
        this.height = height;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public WeiXinUser setWeight(String weight) {
        this.weight = weight;
        return this;
    }

    @Nullable
    public String getWid() {
        return wid;
    }

    public void setWid(@Nullable String wid) {
        this.wid = wid;
    }
}

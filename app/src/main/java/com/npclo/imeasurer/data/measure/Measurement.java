package com.npclo.imeasurer.data.measure;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.npclo.imeasurer.data.IUser;

import java.util.List;

/**
 * @author Endless
 */
public class Measurement {
    public Measurement(@NonNull IUser user, @NonNull List<Part> data, @Nullable String cid) {
        this.data = data;
        this.user = user;
        this.cid = cid;
    }

    @Nullable
    private String id;
    private List<Part> data;
    private IUser user;
    private String uid;
    private String orgId;
    private String cid;

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }
}
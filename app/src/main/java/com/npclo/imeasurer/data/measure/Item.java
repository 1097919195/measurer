package com.npclo.imeasurer.data.measure;

import android.support.annotation.Nullable;

/**
 * @author Endless
 * @date 2017/8/5
 */

public class Item {
    @Nullable
    private String cn;
    @Nullable
    private String name;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getCn() {
        return cn;
    }

    public void setCn(@Nullable String cn) {
        this.cn = cn;
    }
}

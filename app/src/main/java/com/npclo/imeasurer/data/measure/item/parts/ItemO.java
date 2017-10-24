package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemO extends Part {

    public ItemO() {
    }

    public ItemO(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "上凸肚角度";
    }

    @Override
    public String getEn() {
        return "ItemO";
    }
}

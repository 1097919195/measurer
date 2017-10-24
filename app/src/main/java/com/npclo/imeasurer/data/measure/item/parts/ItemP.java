package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemP extends Part {

    public ItemP() {
    }

    public ItemP(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "下凸肚角度";
    }

    @Override
    public String getEn() {
        return "ItemP";
    }
}

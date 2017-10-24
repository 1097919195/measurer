package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemF extends Part {
    public ItemF() {
    }

    public ItemF(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "弯背角度";
    }

    @Override
    public String getEn() {
        return "ItemF";
    }
}

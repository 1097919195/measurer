package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemU extends Part {

    public ItemU() {
    }

    public ItemU(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "小腿围";
    }

    @Override
    public String getEn() {
        return "ItemU";
    }
}

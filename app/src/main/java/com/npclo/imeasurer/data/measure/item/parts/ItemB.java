package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemB extends Part {
    public ItemB() {
    }

    public ItemB(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "颈围";
    }

    @Override
    public String getEn() {
        return "ItemB";
    }
}

package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemR extends Part {

    public ItemR() {
    }

    public ItemR(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "大腿围";
    }

    @Override
    public String getEn() {
        return "ItemR";
    }
}

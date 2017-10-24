package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemQ extends Part {

    public ItemQ() {
    }

    public ItemQ(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "臀围";
    }

    @Override
    public String getEn() {
        return "ItemQ";
    }
}

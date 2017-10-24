package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemX extends Part {

    public ItemX(String cn, String en) {
        super(cn, en);
    }

    public ItemX() {
    }

    @Override
    public String getCn() {
        return "髂骨高度";
    }

    @Override
    public String getEn() {
        return "ItemX";
    }
}

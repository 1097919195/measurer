package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemT extends Part {

    public ItemT() {
    }

    public ItemT(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "膝围";
    }

    @Override
    public String getEn() {
        return "ItemT";
    }
}

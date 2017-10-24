package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemH extends Part {

    public ItemH() {
    }

    public ItemH(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "手腕围";
    }

    @Override
    public String getEn() {
        return "ItemH";
    }
}

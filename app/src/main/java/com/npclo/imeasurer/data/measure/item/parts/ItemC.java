package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemC extends Part {
    public ItemC() {
    }

    public ItemC(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "肩宽";
    }

    @Override
    public String getEn() {
        return "ItemC";
    }
}

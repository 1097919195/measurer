package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemJ extends Part {

    public ItemJ() {
    }

    public ItemJ(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "胸围";
    }

    @Override
    public String getEn() {
        return "ItemJ";
    }
}

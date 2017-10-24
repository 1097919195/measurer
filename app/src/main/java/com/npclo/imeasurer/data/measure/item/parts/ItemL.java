package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemL extends Part {

    public ItemL() {
    }

    public ItemL(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "肚围";
    }

    @Override
    public String getEn() {
        return "ItemL";
    }
}

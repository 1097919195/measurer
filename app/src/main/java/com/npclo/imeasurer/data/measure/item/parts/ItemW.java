package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemW extends Part {

    public ItemW() {
    }

    public ItemW(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "通裆";
    }

    @Override
    public String getEn() {
        return "ItemW";
    }
}

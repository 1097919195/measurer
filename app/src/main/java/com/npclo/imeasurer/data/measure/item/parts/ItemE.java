package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemE extends Part {

    public ItemE() {
    }

    public ItemE(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "右肩斜";
    }

    @Override
    public String getEn() {
        return "ItemE";
    }
}

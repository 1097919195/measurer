package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemV extends Part {
    public ItemV() {
    }

    public ItemV(String cn, String en) {
        super(cn, en);
    }
//    private String cn = "脚踝围";
//    private String en = "ItemV";

    @Override
    public String getCn() {
        return "脚踝围";
    }

    @Override
    public String getEn() {
        return "ItemV";
    }
}

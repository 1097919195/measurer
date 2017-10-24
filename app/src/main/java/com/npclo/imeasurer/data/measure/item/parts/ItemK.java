package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemK extends Part {
    public ItemK() {
    }

    public ItemK(String cn, String en) {
        super(cn, en);
    }
//    private String cn = "下胸围";
//    private String en = "ItemK";

    @Override
    public String getCn() {
        return "下胸围";
    }

    @Override
    public String getEn() {
        return "ItemK";
    }
}

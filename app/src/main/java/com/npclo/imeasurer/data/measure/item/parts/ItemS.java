package com.npclo.imeasurer.data.measure.item.parts;

/**
 *
 * @author Endless
 * @date 2017/8/5
 */

public class ItemS extends Part {

    public ItemS() {
    }

    public ItemS(String cn, String en) {
        super(cn, en);
    }

    @Override
    public String getCn() {
        return "中部大腿围";
    }

    @Override
    public String getEn() {
        return "ItemS";
    }
}

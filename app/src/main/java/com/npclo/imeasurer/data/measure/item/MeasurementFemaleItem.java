package com.npclo.imeasurer.data.measure.item;

import com.npclo.imeasurer.data.measure.item.parts.ItemK;

public class MeasurementFemaleItem extends MeasurementItem {
    private ItemK ItemK;

    @Override
    public ItemK getItemK() {
        return ItemK;
    }

    @Override
    public MeasurementFemaleItem setItemK(ItemK itemK) {
        ItemK = itemK;
        return this;
    }
}
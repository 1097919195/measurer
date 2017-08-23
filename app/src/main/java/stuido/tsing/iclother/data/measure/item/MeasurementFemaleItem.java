package stuido.tsing.iclother.data.measure.item;

import stuido.tsing.iclother.data.measure.item.parts.ItemK;

public class MeasurementFemaleItem extends MeasurementItem {
    private ItemK ItemK;

    public ItemK getItemK() {
        return ItemK;
    }

    public MeasurementFemaleItem setItemK(ItemK itemK) {
        ItemK = itemK;
        return this;
    }
}
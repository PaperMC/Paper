package io.papermc.paper.datacomponent.item.attribute;

import net.minecraft.world.item.component.ItemAttributeModifiers;

public record PaperDefaultDisplay(
    ItemAttributeModifiers.Display.Default impl
) implements AttributeModifierDisplay.Default, PaperAttributeModifierDisplay<ItemAttributeModifiers.Display.Default> {

    @Override
    public ItemAttributeModifiers.Display.Default getHandle() {
        return this.impl;
    }
}

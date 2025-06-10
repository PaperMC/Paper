package io.papermc.paper.datacomponent.item.attribute;

import net.minecraft.world.item.component.ItemAttributeModifiers;

public record PaperHiddenDisplay(
    ItemAttributeModifiers.Display.Hidden impl
) implements AttributeModifierDisplay.Hidden, PaperAttributeModifierDisplay<ItemAttributeModifiers.Display.Hidden> {

    @Override
    public ItemAttributeModifiers.Display.Hidden getHandle() {
        return this.impl;
    }
}

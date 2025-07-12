package io.papermc.paper.datacomponent.item.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public record PaperOverrideTextDisplay(
    ItemAttributeModifiers.Display.OverrideText impl
) implements AttributeModifierDisplay.OverrideText, PaperAttributeModifierDisplay<ItemAttributeModifiers.Display.OverrideText> {

    @Override
    public Component text() {
        return PaperAdventure.asAdventure(this.impl.component());
    }

    @Override
    public ItemAttributeModifiers.Display.OverrideText getHandle() {
        return this.impl;
    }
}

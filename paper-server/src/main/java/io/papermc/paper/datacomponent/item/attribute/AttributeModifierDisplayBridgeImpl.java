package io.papermc.paper.datacomponent.item.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.ComponentLike;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AttributeModifierDisplayBridgeImpl implements AttributeModifierDisplayBridge {

    @Override
    public AttributeModifierDisplay.Default reset() {
        return PaperAttributeModifierDisplay.Default.INSTANCE;
    }

    @Override
    public AttributeModifierDisplay.Hidden hidden() {
        return PaperAttributeModifierDisplay.Hidden.INSTANCE;
    }

    @Override
    public AttributeModifierDisplay.OverrideText override(final ComponentLike text) {
        return new PaperAttributeModifierDisplay.OverrideText(new ItemAttributeModifiers.Display.OverrideText(PaperAdventure.asVanilla(text.asComponent())));
    }
}

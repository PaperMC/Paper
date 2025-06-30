package io.papermc.paper.datacomponent.item.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.ComponentLike;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AttributeModifierDisplayBridgeImpl implements AttributeModifierDisplayBridge {

    @Override
    public AttributeModifierDisplay.Default reset() {
        return new PaperDefaultDisplay(new ItemAttributeModifiers.Display.Default());
    }

    @Override
    public AttributeModifierDisplay.Hidden hidden() {
        return new PaperHiddenDisplay(new ItemAttributeModifiers.Display.Hidden());
    }

    @Override
    public AttributeModifierDisplay.OverrideText override(final ComponentLike text) {
        return new PaperOverrideTextDisplay(new ItemAttributeModifiers.Display.OverrideText(PaperAdventure.asVanilla(text.asComponent())));
    }
}

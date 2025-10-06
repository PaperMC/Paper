package io.papermc.paper.datacomponent.item.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public sealed interface PaperAttributeModifierDisplay permits PaperAttributeModifierDisplay.Default, PaperAttributeModifierDisplay.Hidden, PaperAttributeModifierDisplay.OverrideText {

    static AttributeModifierDisplay fromVanilla(ItemAttributeModifiers.Display display) {
        return switch (display) {
            case ItemAttributeModifiers.Display.Default $ -> Default.INSTANCE;
            case ItemAttributeModifiers.Display.Hidden $ -> Hidden.INSTANCE;
            case ItemAttributeModifiers.Display.OverrideText override -> new OverrideText(override);
            default -> throw new UnsupportedOperationException("Don't know how to convert " + display.getClass());
        };
    }

    static ItemAttributeModifiers.Display toVanilla(AttributeModifierDisplay display) {
        if (display instanceof PaperAttributeModifierDisplay paperDisplay) {
            return paperDisplay.internal();
        } else {
            throw new UnsupportedOperationException("Must implement PaperAttributeModifierDisplay!");
        }
    }

    ItemAttributeModifiers.Display internal();

    record Default(
        ItemAttributeModifiers.Display internal
    ) implements AttributeModifierDisplay.Default, PaperAttributeModifierDisplay {

        public static final PaperAttributeModifierDisplay.Default INSTANCE = new PaperAttributeModifierDisplay.Default(ItemAttributeModifiers.Display.attributeModifiers());
    }

    record Hidden(
        ItemAttributeModifiers.Display internal
    ) implements AttributeModifierDisplay.Hidden, PaperAttributeModifierDisplay {

        public static final PaperAttributeModifierDisplay.Hidden INSTANCE = new PaperAttributeModifierDisplay.Hidden(ItemAttributeModifiers.Display.hidden());
    }

    record OverrideText(
        ItemAttributeModifiers.Display.OverrideText internal
    ) implements AttributeModifierDisplay.OverrideText, PaperAttributeModifierDisplay {

        @Override
        public Component text() {
            return PaperAdventure.asAdventure(this.internal.component());
        }
    }
}

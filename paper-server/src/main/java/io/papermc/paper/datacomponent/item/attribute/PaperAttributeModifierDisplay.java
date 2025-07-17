package io.papermc.paper.datacomponent.item.attribute;

import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.bukkit.craftbukkit.util.Handleable;

public interface PaperAttributeModifierDisplay<T extends ItemAttributeModifiers.Display> extends Handleable<T> {

    static AttributeModifierDisplay fromNms(ItemAttributeModifiers.Display display) {
        return switch (display) {
            case ItemAttributeModifiers.Display.Default def -> new PaperDefaultDisplay(def);
            case ItemAttributeModifiers.Display.Hidden hidden -> new PaperHiddenDisplay(hidden);
            case ItemAttributeModifiers.Display.OverrideText override -> new PaperOverrideTextDisplay(override);
            default -> throw new UnsupportedOperationException("Don't know how to convert " + display.getClass());
        };
    }

    static ItemAttributeModifiers.Display toNms(AttributeModifierDisplay display) {
        if (display instanceof PaperAttributeModifierDisplay<?> modifierDisplay) {
            return modifierDisplay.getHandle();
        } else {
            throw new UnsupportedOperationException("Must implement handleable!");
        }
    }
}

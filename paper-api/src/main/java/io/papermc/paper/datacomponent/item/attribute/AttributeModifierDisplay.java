package io.papermc.paper.datacomponent.item.attribute;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * The display behavior for a dedicated attribute entry.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#ATTRIBUTE_MODIFIERS
 * @see io.papermc.paper.datacomponent.item.ItemAttributeModifiers#itemAttributes()
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface AttributeModifierDisplay {

    /**
     * Reset any override of the text displayed by the attribute modifier
     * to its default behavior displaying the statistics.
     *
     * @return the new display behavior instance
     */
    @Contract(value = "-> new", pure = true)
    static Default reset() {
        return AttributeModifierDisplayBridge.bridge().reset();
    }

    /**
     * Hides the statistics displayed by the attribute modifier.
     *
     * @return the new display behavior instance
     */
    @Contract(value = "-> new", pure = true)
    static Hidden hidden() {
        return AttributeModifierDisplayBridge.bridge().hidden();
    }

    /**
     * Override the statistics displayed by the attribute modifier
     * to an arbitrary text.
     *
     * @param text the overridden text
     * @return the new display behavior instance
     */
    @Contract(value = "_ -> new", pure = true)
    static OverrideText override(final ComponentLike text) {
        return AttributeModifierDisplayBridge.bridge().override(text);
    }

    /**
     * Hidden statistics display for the attribute modifier.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Hidden extends AttributeModifierDisplay {
    }

    /**
     * Default display for the attribute modifier, showing
     * the statistic of its effect.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Default extends AttributeModifierDisplay {
    }

    /**
     * Specifies an overridden text to show instead of
     * the default behavior for the attribute modifier.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface OverrideText extends AttributeModifierDisplay {

        /**
         * Overridden text
         *
         * @return the overridden text
         */
        Component text();
    }
}

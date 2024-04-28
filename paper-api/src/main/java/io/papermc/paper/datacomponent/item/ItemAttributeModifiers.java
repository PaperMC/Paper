package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds attribute modifiers applied to any item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#ATTRIBUTE_MODIFIERS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemAttributeModifiers extends ShownInTooltip<ItemAttributeModifiers> {

    @Contract(value = "-> new", pure = true)
    static ItemAttributeModifiers.Builder itemAttributes() {
        return ItemComponentTypesBridge.bridge().modifiers();
    }

    /**
     * Lists the attribute modifiers that are present on this item.
     *
     * @return modifiers
     */
    @Contract(pure = true)
    @Unmodifiable List<Entry> modifiers();

    /**
     * Holds an attribute entry.
     */
    @ApiStatus.NonExtendable
    interface Entry {

        /**
         * Gets the target attribute for the paired modifier.
         *
         * @return the attribute
         */
        @Contract(pure = true)
        Attribute attribute();

        /**
         * The modifier for the paired attribute.
         *
         * @return the modifier
         */
        @Contract(pure = true)
        AttributeModifier modifier();

        /**
         * Gets the slot group for this attribute.
         *
         * @return the slot group
         */
        default EquipmentSlotGroup getGroup() {
            return this.modifier().getSlotGroup();
        }
    }

    /**
     * Builder for {@link ItemAttributeModifiers}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ShownInTooltip.Builder<Builder>, DataComponentBuilder<ItemAttributeModifiers> {

        /**
         * Adds a modifier to this builder.
         *
         * @param attribute attribute
         * @param modifier  modifier
         * @return the builder for chaining
         * @see #modifiers()
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        Builder addModifier(Attribute attribute, AttributeModifier modifier);

        /**
         * Adds a modifier to this builder.
         *
         * @param attribute          attribute
         * @param modifier           modifier
         * @param equipmentSlotGroup the slot group this modifier applies to (overrides any slot group in the modifier)
         * @return the builder for chaining
         * @see #modifiers()
         */
        @Contract(value = "_, _, _ -> this", mutates = "this")
        Builder addModifier(Attribute attribute, AttributeModifier modifier, EquipmentSlotGroup equipmentSlotGroup);
    }
}

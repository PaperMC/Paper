package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.item.attribute.AttributeModifierDisplay;
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
public interface ItemAttributeModifiers {

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
         * Gets the slot group for the paired attribute.
         *
         * @return the slot group
         */
        default EquipmentSlotGroup getGroup() {
            return this.modifier().getSlotGroup();
        }

        /**
         * The display behavior for the attribute modifier.
         *
         * @return the display behavior
         */
        @Contract(pure = true)
        AttributeModifierDisplay display();
    }

    /**
     * Builder for {@link ItemAttributeModifiers}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemAttributeModifiers> {

        /**
         * Adds a modifier to this builder.
         *
         * @param attribute the attribute
         * @param modifier  the modifier
         * @return the builder for chaining
         * @see #modifiers()
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        default Builder addModifier(Attribute attribute, AttributeModifier modifier) {
            return this.addModifier(attribute, modifier, modifier.getSlotGroup());
        }

        /**
         * Adds a modifier to this builder.
         *
         * @param attribute          the attribute
         * @param modifier           the modifier
         * @param equipmentSlotGroup the slot group this modifier applies to (overrides any slot group in the modifier)
         * @return the builder for chaining
         * @see #modifiers()
         */
        @Contract(value = "_, _, _ -> this", mutates = "this")
        default Builder addModifier(Attribute attribute, AttributeModifier modifier, EquipmentSlotGroup equipmentSlotGroup) {
            return this.addModifier(attribute, modifier, equipmentSlotGroup, AttributeModifierDisplay.reset());
        }

        /**
         * Adds a modifier to this builder.
         *
         * @param attribute          the attribute
         * @param modifier           the modifier
         * @param display            the modifier display behavior
         * @return the builder for chaining
         * @see #modifiers()
         */
        @Contract(value = "_, _, _ -> this", mutates = "this")
        default Builder addModifier(Attribute attribute, AttributeModifier modifier, AttributeModifierDisplay display) {
            return this.addModifier(attribute, modifier, modifier.getSlotGroup(), display);
        }

        /**
         * Adds a modifier to this builder.
         *
         * @param attribute          the attribute
         * @param modifier           the modifier
         * @param equipmentSlotGroup the slot group this modifier applies to (overrides any slot group in the modifier)
         * @param display            the modifier display behavior
         * @return the builder for chaining
         * @see #modifiers()
         */
        @Contract(value = "_, _, _, _ -> this", mutates = "this")
        Builder addModifier(Attribute attribute, AttributeModifier modifier, EquipmentSlotGroup equipmentSlotGroup, AttributeModifierDisplay display);
    }
}

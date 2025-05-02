package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the trims applied to an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#TRIM
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemArmorTrim  {

    @Contract(value = "_ -> new", pure = true)
    static ItemArmorTrim.Builder itemArmorTrim(final ArmorTrim armorTrim) {
        return ItemComponentTypesBridge.bridge().itemArmorTrim(armorTrim);
    }

    /**
     * Armor trim present on this item.
     *
     * @return trim
     */
    @Contract(pure = true)
    ArmorTrim armorTrim();

    /**
     * Builder for {@link ItemArmorTrim}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemArmorTrim> {

        /**
         * Sets the armor trim for this builder.
         *
         * @param armorTrim trim
         * @return the builder for chaining
         * @see #armorTrim()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder armorTrim(ArmorTrim armorTrim);
    }
}

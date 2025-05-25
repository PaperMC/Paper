package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Stores a list of enchantments and their levels on an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#ENCHANTMENTS
 * @see io.papermc.paper.datacomponent.DataComponentTypes#STORED_ENCHANTMENTS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemEnchantments {

    @Contract(value = "_, _ -> new", pure = true)
    static ItemEnchantments itemEnchantments(final Map<Enchantment, @IntRange(from = 1, to = 255) Integer> enchantments) {
        return itemEnchantments().addAll(enchantments).build();
    }

    @Contract(value = "-> new", pure = true)
    static ItemEnchantments.Builder itemEnchantments() {
        return ItemComponentTypesBridge.bridge().enchantments();
    }

    /**
     * Enchantments currently present on this item.
     *
     * @return enchantments
     */
    @Contract(pure = true)
    @Unmodifiable Map<Enchantment, @IntRange(from = 1, to = 255) Integer> enchantments();

    /**
     * Builder for {@link ItemEnchantments}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ItemEnchantments> {

        /**
         * Adds an enchantment with the given level to this component.
         *
         * @param enchantment enchantment
         * @param level level
         * @return the builder for chaining
         * @see #enchantments()
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        Builder add(Enchantment enchantment, @IntRange(from = 1, to = 255) int level);

        /**
         * Adds enchantments with the given level to this component.
         *
         * @param enchantments enchantments
         * @return the builder for chaining
         * @see #enchantments()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(Map<Enchantment, @IntRange(from = 1, to = 255) Integer> enchantments);
    }
}

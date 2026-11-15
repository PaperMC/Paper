package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.TypedKey;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Stores a list of enchantments and their levels on an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#ENCHANTMENTS
 * @see io.papermc.paper.datacomponent.DataComponentTypes#STORED_ENCHANTMENTS
 */
@ApiStatus.NonExtendable
public interface ItemEnchantments {

    @Contract(value = "_ -> new", pure = true)
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
     * Keys of the enchantments currently present on this item.
     * <p>
     * Unlike {@link #enchantments()}, this is available while the enchantment
     * registry is still being loaded, such as in registry events.
     *
     * @return enchantment keys and their levels
     */
    @Contract(pure = true)
    @Unmodifiable Map<TypedKey<Enchantment>, @IntRange(from = 1, to = 255) Integer> enchantmentKeys();

    /**
     * Builder for {@link ItemEnchantments}.
     */
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
         * Adds an enchantment with the given level to this component.
         * <p>
         * Unlike {@link #add(Enchantment, int)}, this can be used while the enchantment
         * registry is still being loaded, such as in registry events.
         *
         * @param enchantment enchantment key
         * @param level level
         * @return the builder for chaining
         * @see #enchantmentKeys()
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        Builder add(TypedKey<Enchantment> enchantment, @IntRange(from = 1, to = 255) int level);

        /**
         * Adds enchantments with the given level to this component.
         *
         * @param enchantments enchantments
         * @return the builder for chaining
         * @see #enchantments()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(Map<Enchantment, @IntRange(from = 1, to = 255) Integer> enchantments);

        /**
         * Adds enchantments with the given level to this component.
         * <p>
         * Unlike {@link #addAll(Map)}, this can be used while the enchantment
         * registry is still being loaded, such as in registry events.
         *
         * @param enchantments enchantment keys and their levels
         * @return the builder for chaining
         * @see #enchantmentKeys()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAllKeys(Map<TypedKey<Enchantment>, @IntRange(from = 1, to = 255) Integer> enchantments);
    }
}

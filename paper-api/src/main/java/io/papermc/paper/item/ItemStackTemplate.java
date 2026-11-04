package io.papermc.paper.item;

import io.papermc.paper.datacomponent.DataComponentMutable;
import java.util.function.Consumer;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds the blueprint for constructing a new {@link ItemStack}, used primarily to represent items
 * before registries are loaded.
 * <p>
 * This class only holds a subset of the methods a regular {@link ItemStack} holds. It is not intended
 * to be used as a general-purpose class to create {@link ItemStack}s.
 *
 * @see ItemStack
 */
@NullMarked
public interface ItemStackTemplate extends DataComponentMutable, Translatable, HoverEventSource<HoverEvent.ShowItem> {

    static ItemStackTemplate of(final ItemType type) {
        return of(type, 1, null);
    }

    static ItemStackTemplate of(final ItemType type, final int amount) {
        return of(type, amount, null);
    }

    static ItemStackTemplate of(final ItemType type, final @Nullable Consumer<ItemStackTemplate> apply) {
        return of(type, 1, apply);
    }

    static ItemStackTemplate of(final ItemType type, final int amount, final @Nullable Consumer<ItemStackTemplate> apply) {
        final ItemStackTemplate template = type.createItemStackTemplate(amount);
        if (apply != null) {
            apply.accept(template);
        }
        return template;
    }

    int count();

    ItemType type();

    ItemStackTemplate withCount(int amount);

    ItemStackTemplate withType(ItemType type);

    /**
     * {@return this template resolved to an {@link ItemStack}}
     */
    ItemStack getAsItemStack();
}

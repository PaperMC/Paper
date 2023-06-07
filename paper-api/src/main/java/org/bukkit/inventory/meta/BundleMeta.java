package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BundleMeta extends ItemMeta {

    /**
     * Returns whether the item has any items.
     *
     * @return whether items are present
     */
    boolean hasItems();

    /**
     * Returns an immutable list of the items stored in this item.
     *
     * @return items
     */
    @NotNull
    List<ItemStack> getItems();

    /**
     * Sets the items stored in this item.
     *
     * Removes all items when given null.
     *
     * @param items the items to set
     */
    void setItems(@Nullable List<ItemStack> items);

    /**
     * Adds an item to this item.
     *
     * @param item item to add
     */
    void addItem(@NotNull ItemStack item);
}

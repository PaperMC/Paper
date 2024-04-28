package io.papermc.paper.datacomponent.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the contents of item transformation information when an item is used.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#USE_REMAINDER
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface UseRemainder {

    @Contract(value = "_ -> new", pure = true)
    static UseRemainder useRemainder(final ItemStack itemStack) {
        return ItemComponentTypesBridge.bridge().useRemainder(itemStack);
    }

    /**
     * The item that the item that is consumed is transformed into.
     *
     * @return item
     */
    @Contract(value = "-> new", pure = true)
    ItemStack transformInto();
}

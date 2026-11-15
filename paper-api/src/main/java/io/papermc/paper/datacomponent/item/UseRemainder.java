package io.papermc.paper.datacomponent.item;

import io.papermc.paper.item.ItemStackTemplate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Holds the contents of item transformation information when an item is used.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#USE_REMAINDER
 */
@ApiStatus.NonExtendable
public interface UseRemainder {

    @Contract(value = "_ -> new", pure = true)
    static UseRemainder useRemainder(final ItemStack itemStack) {
        return useRemainder(itemStack.toTemplate());
    }

    @Contract(value = "_ -> new", pure = true)
    static UseRemainder useRemainder(final ItemStackTemplate template) {
        return ItemComponentTypesBridge.bridge().useRemainder(template);
    }

    /**
     * The item that the item that is consumed is transformed into.
     *
     * @return item
     * @see #transformIntoTemplate()
     */
    @Contract(value = "-> new", pure = true)
    ItemStack transformInto();

    /**
     * The item that the item that is consumed is transformed into.
     * <p>
     * Unlike {@link #transformInto()}, this is available while registries are
     * still being loaded, such as in registry events.
     *
     * @return item
     */
    @Contract(pure = true)
    ItemStackTemplate transformIntoTemplate();
}

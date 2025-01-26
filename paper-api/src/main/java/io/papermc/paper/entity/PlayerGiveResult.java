package io.papermc.paper.entity;

import java.util.Collection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * A result type used by {@link org.bukkit.entity.Player#give(ItemStack...)} and its overloads.
 */
@NullMarked
public interface PlayerGiveResult {

    /**
     * A collection of itemstacks that were not added to the player's inventory as they did not fit.
     * The collection is derived from the collections of items to add by creating copies of each stack that was not
     * fully added to the inventory and assigning the non-added count as their amount.
     * <p>
     * Itemstacks found here *may* also be found as item entities in the {@link #drops()} collection, as the
     * give logic may have dropped them.
     *
     * @return the unmodifiable collection of itemstacks that are leftover as they could not be added. Each element is a
     * copy of the input stack they are derived from.
     */
    @Unmodifiable
    Collection<ItemStack> leftovers();

    /**
     * A collection of item entities dropped as a result of this call to {@link org.bukkit.entity.Player#give(ItemStack...)}.
     * The item entities contained here are not guaranteed to match the {@link #leftovers()} as plugins may cancel the
     * spawning of item entities.
     *
     * @return the unmodifiable collection of dropped item entities.
     */
    @Unmodifiable
    Collection<Item> drops();

}

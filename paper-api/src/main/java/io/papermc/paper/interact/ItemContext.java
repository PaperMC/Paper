package io.papermc.paper.interact;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Used to provide additional item-related context for {@link io.papermc.paper.interact.InteractionResult.Success} results.
 */
@NullMarked
public interface ItemContext {

    /**
     * Returns if the related interaction involved an item.
     *
     * @return if the related interaction involved an item
     */
    boolean wasItemInteraction();

    /**
     * Returns what the item used in the interaction was transformed into after the interaction was performed. Will be
     * <code>null</code> if the item was not transformed or if {@link #wasItemInteraction()} is <code>false</code>.
     *
     * @return the transformed item
     */
    @Nullable
    ItemStack heldItemTransformedTo();

}

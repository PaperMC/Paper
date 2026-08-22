package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.entity.Piglin;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Stores all data related to the bartering interaction with a piglin.
 * <br>
 * Called when a piglin completes a barter.
 */
public interface PiglinBarterEvent extends EntityEvent, Cancellable {

    @Override
    Piglin getEntity();

    /**
     * Gets the input of the barter.
     *
     * @return The item that was used to barter with
     */
    ItemStack getInput();

    /**
     * Returns a mutable list representing the outcome of the barter.
     *
     * @return A mutable list of the item the player will receive
     */
    List<ItemStack> getOutcome();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

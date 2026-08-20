package org.bukkit.event.inventory;

import java.util.List;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Called when the brewing of the contents inside the Brewing Stand is
 * complete.
 */
public interface BrewEvent extends BlockEvent, Cancellable {

    /**
     * Gets the contents of the Brewing Stand.
     *
     * @return the contents
     * @apiNote The brewer inventory still holds the items found prior to
     * the finalization of the brewing process, e.g. the plain water bottles.
     */
    BrewerInventory getContents();

    /**
     * Gets the resulting items in the Brewing Stand.
     * <p>
     * The returned list, in case of a server-created event instance, is
     * mutable. Any changes in the returned list will reflect in the brewing
     * result if the event is not cancelled. If the size of the list is reduced,
     * remaining items will be set to air.
     *
     * @return List of {@link ItemStack} resulting for this operation
     */
    List<ItemStack> getResults();

    /**
     * Gets the remaining fuel level.
     *
     * @return the remaining fuel
     */
    int getFuelLevel();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

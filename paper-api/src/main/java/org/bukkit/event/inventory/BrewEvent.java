package org.bukkit.event.inventory;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the brewing of the contents inside the Brewing Stand is
 * complete.
 */
public class BrewEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private BrewerInventory contents;
    private final List<ItemStack> results;
    private int fuelLevel;
    private boolean cancelled;

    public BrewEvent(@NotNull Block brewer, @NotNull BrewerInventory contents, @NotNull List<ItemStack> results, int fuelLevel) {
        super(brewer);
        this.contents = contents;
        this.results = results;
        this.fuelLevel = fuelLevel;
    }

    /**
     * Gets the contents of the Brewing Stand.
     *
     * <b>Note:</b> The brewer inventory still holds the items found prior to
     * the finalization of the brewing process, e.g. the plain water bottles.
     *
     * @return the contents
     */
    @NotNull
    public BrewerInventory getContents() {
        return contents;
    }

    /**
     * Gets the remaining fuel level.
     *
     * @return the remaining fuel
     */
    public int getFuelLevel() {
        return fuelLevel;
    }

    /**
     * Gets the resulting items in the Brewing Stand.
     *
     * The returned list, in case of a server-created event instance, is
     * mutable. Any changes in the returned list will reflect in the brewing
     * result if the event is not cancelled. If the size of the list is reduced,
     * remaining items will be set to air.
     *
     * @return List of {@link ItemStack} resulting for this operation
     */
    @NotNull
    public List<ItemStack> getResults() {
        return results;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

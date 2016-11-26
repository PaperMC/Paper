package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.BrewerInventory;

/**
 * Called when the brewing of the contents inside the Brewing Stand is
 * complete.
 */
public class BrewEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private BrewerInventory contents;
    private int fuelLevel;
    private boolean cancelled;

    public BrewEvent(Block brewer, BrewerInventory contents, int fuelLevel) {
        super(brewer);
        this.contents = contents;
        this.fuelLevel = fuelLevel;
    }

    /**
     * Gets the contents of the Brewing Stand.
     *
     * @return the contents
     */
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

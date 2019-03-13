package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.BrewerInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the brewing of the contents inside the Brewing Stand is
 * complete.
 */
public class BrewEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private BrewerInventory contents;
    private int fuelLevel;
    private boolean cancelled;

    public BrewEvent(@NotNull Block brewer, @NotNull BrewerInventory contents, int fuelLevel) {
        super(brewer);
        this.contents = contents;
        this.fuelLevel = fuelLevel;
    }

    /**
     * Gets the contents of the Brewing Stand.
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

    public boolean isCancelled() {
        return cancelled;
    }

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

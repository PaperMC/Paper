package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully burned as fuel in a furnace.
 */
public class FurnaceBurnEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack fuel;
    private int burnTime;
    private boolean cancelled;
    private boolean burning;

    public FurnaceBurnEvent(@NotNull final Block furnace, @NotNull final ItemStack fuel, final int burnTime) {
        super(furnace);
        this.fuel = fuel;
        this.burnTime = burnTime;
        this.cancelled = false;
        this.burning = true;
    }

    /**
     * Gets the fuel ItemStack for this event
     *
     * @return the fuel ItemStack
     */
    @NotNull
    public ItemStack getFuel() {
        return fuel;
    }

    /**
     * Gets the burn time for this fuel
     *
     * @return the burn time for this fuel
     */
    public int getBurnTime() {
        return burnTime;
    }

    /**
     * Sets the burn time for this fuel
     *
     * @param burnTime the burn time for this fuel
     */
    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    /**
     * Gets whether the furnace's fuel is burning or not.
     *
     * @return whether the furnace's fuel is burning or not.
     */
    public boolean isBurning() {
        return this.burning;
    }

    /**
     * Sets whether the furnace's fuel is burning or not.
     *
     * @param burning true if the furnace's fuel is burning
     */
    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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

package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

public class CraftFurnaceBurnEvent extends CraftBlockEvent implements FurnaceBurnEvent {

    private final ItemStack fuel;
    private int burnTime;
    private boolean burning = true;
    private boolean consumeFuel = true;

    private boolean cancelled;

    public CraftFurnaceBurnEvent(final Block furnace, final ItemStack fuel, final int burnTime) {
        super(furnace);
        this.fuel = fuel;
        this.burnTime = burnTime;
    }

    @Override
    public ItemStack getFuel() {
        return this.fuel;
    }

    @Override
    public int getBurnTime() {
        return this.burnTime;
    }

    @Override
    public void setBurnTime(final @Range(from = Short.MIN_VALUE, to = Short.MAX_VALUE) int burnTime) {
        this.burnTime = Math.clamp(burnTime, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    public boolean isBurning() {
        return this.burning;
    }

    @Override
    public void setBurning(final boolean burning) {
        this.burning = burning;
    }

    @Override
    public boolean willConsumeFuel() {
        return this.consumeFuel;
    }

    @Override
    public void setConsumeFuel(final boolean consumeFuel) {
        this.consumeFuel = consumeFuel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return FurnaceBurnEvent.getHandlerList();
    }
}

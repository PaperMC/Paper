package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is about to increase the fuel level of a brewing
 * stand.
 */
public class BrewingStandFuelEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack fuel;
    private int fuelPower;
    private boolean consuming = true;

    private boolean cancelled;

    @ApiStatus.Internal
    public BrewingStandFuelEvent(@NotNull Block brewingStand, @NotNull ItemStack fuel, int fuelPower) {
        super(brewingStand);
        this.fuel = fuel;
        this.fuelPower = fuelPower;
    }

    /**
     * Gets the ItemStack of the fuel before the amount was subtracted.
     *
     * @return the fuel ItemStack
     */
    @NotNull
    public ItemStack getFuel() {
        return this.fuel;
    }

    /**
     * Gets the fuel power for this fuel. Each unit of power can fuel one
     * brewing operation.
     *
     * @return the fuel power for this fuel
     */
    public int getFuelPower() {
        return this.fuelPower;
    }

    /**
     * Sets the fuel power for this fuel. Each unit of power can fuel one
     * brewing operation.
     *
     * @param fuelPower the fuel power for this fuel
     */
    public void setFuelPower(int fuelPower) {
        this.fuelPower = fuelPower;
    }

    /**
     * Gets whether the brewing stand's fuel will be reduced / consumed or not.
     *
     * @return whether the fuel will be reduced or not
     */
    public boolean isConsuming() {
        return this.consuming;
    }

    /**
     * Sets whether the brewing stand's fuel will be reduced / consumed or not.
     *
     * @param consuming whether the fuel will be reduced or not
     */
    public void setConsuming(boolean consuming) {
        this.consuming = consuming;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.ItemStack;

public class CraftBrewingStandFuelEvent extends CraftBlockEvent implements BrewingStandFuelEvent {

    private final ItemStack fuel;
    private int fuelPower;
    private boolean consuming = true;

    private boolean cancelled;

    public CraftBrewingStandFuelEvent(final Block brewingStand, final ItemStack fuel, final int fuelPower) {
        super(brewingStand);
        this.fuel = fuel;
        this.fuelPower = fuelPower;
    }

    public CraftBrewingStandFuelEvent(final Level level, final BlockPos pos, final net.minecraft.world.item.ItemStack fuel, final int fuelPower) {
        this(CraftBlock.at(level, pos), CraftItemStack.asCraftMirror(fuel), fuelPower);
    }

    @Override
    public ItemStack getFuel() {
        return this.fuel;
    }

    @Override
    public int getFuelPower() {
        return this.fuelPower;
    }

    @Override
    public void setFuelPower(final int fuelPower) {
        this.fuelPower = fuelPower;
    }

    @Override
    public boolean isConsuming() {
        return this.consuming;
    }

    @Override
    public void setConsuming(final boolean consuming) {
        this.consuming = consuming;
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
        return BrewingStandFuelEvent.getHandlerList();
    }
}

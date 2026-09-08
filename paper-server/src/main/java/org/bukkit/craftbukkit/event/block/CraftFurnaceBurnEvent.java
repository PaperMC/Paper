package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

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

    public CraftFurnaceBurnEvent(final Level level, final BlockPos pos, final net.minecraft.world.item.ItemStack fuel, final int burnTime) {
        this(CraftBlock.at(level, pos), CraftItemStack.asCraftMirror(fuel), burnTime);
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
    public void setBurnTime(final @IntRange(from = Short.MIN_VALUE, to = Short.MAX_VALUE) int burnTime) {
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

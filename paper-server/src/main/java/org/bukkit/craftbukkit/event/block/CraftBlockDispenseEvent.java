package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.phys.Vec3;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CraftBlockDispenseEvent extends CraftBlockEvent implements BlockDispenseEvent {

    private ItemStack dispensed;
    private Vector velocity;

    private boolean cancelled;

    public CraftBlockDispenseEvent(final Block block, final ItemStack dispensed, final Vector velocity) {
        super(block);
        this.dispensed = dispensed;
        this.velocity = velocity;
    }

    public CraftBlockDispenseEvent(final BlockSource pointer, final net.minecraft.world.item.ItemStack dispensed, final Vector velocity) {
        this(
            CraftBlock.at(pointer.level(), pointer.pos()),
            CraftItemStack.asCraftMirror(dispensed.isDamageableItem() ? dispensed : dispensed.copyWithCount(1)),
            velocity
        );
    }

    public CraftBlockDispenseEvent(final BlockSource pointer, final net.minecraft.world.item.ItemStack dispensed, final Vec3 movement) {
        this(pointer, dispensed, CraftVector.toBukkit(movement));
    }

    public CraftBlockDispenseEvent(final BlockSource pointer, final net.minecraft.world.item.ItemStack dispensed, final BlockPos to) {
        this(pointer, dispensed, CraftVector.toBukkit(to));
    }

    public CraftBlockDispenseEvent(final BlockSource pointer, final net.minecraft.world.item.ItemStack dispensed, final double x, final double y, final double z) {
        this(pointer, dispensed, new Vector(x, y, z));
    }

    @Override
    public ItemStack getItem() {
        return this.dispensed.clone();
    }

    @Override
    public void setItem(final ItemStack item) {
        this.dispensed = item;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity.clone();
    }

    @Override
    public void setVelocity(final Vector velocity) {
        this.velocity = velocity.clone();
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
        return BlockDispenseEvent.getHandlerList();
    }
}

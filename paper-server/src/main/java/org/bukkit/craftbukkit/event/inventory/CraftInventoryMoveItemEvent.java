package org.bukkit.craftbukkit.event.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryMoveItemEvent extends CraftEvent implements InventoryMoveItemEvent {

    private final Inventory sourceInventory;
    private final Inventory destinationInventory;
    private ItemStack itemStack;
    private final boolean didSourceInitiate;

    private boolean cancelled;

    public boolean calledSetItem;
    public boolean calledGetItem;

    public CraftInventoryMoveItemEvent(final Inventory sourceInventory, final ItemStack itemStack, final Inventory destinationInventory, final boolean didSourceInitiate) {
        this.sourceInventory = sourceInventory;
        this.itemStack = itemStack;
        this.destinationInventory = destinationInventory;
        this.didSourceInitiate = didSourceInitiate;
    }

    public CraftInventoryMoveItemEvent(
        final Container source,
        final net.minecraft.world.item.ItemStack itemStack,
        final Container destination,
        final boolean didSourceInitiate,
        final boolean fromHopper
    ) {
        this(
            extractInventory(source, fromHopper),
            CraftItemStack.asCraftMirror(itemStack),
            extractInventory(destination, fromHopper),
            didSourceInitiate
        );
    }

    public static Inventory extractInventory(final Container container, final boolean liveView) {
        if (container instanceof final CompoundContainer compoundContainer) { // Have to special-case large chests as they work oddly
            return new CraftInventoryDoubleChest(compoundContainer);
        }

        final InventoryHolder owner;
        if (liveView && container instanceof final BlockEntity blockEntity) {
            owner = blockEntity.getOwner(false);
        } else {
            owner = container.getOwner();
        }
        if (owner != null) {
            return owner.getInventory();
        } else {
            return new CraftInventory(container);
        }
    }

    @Override
    public Inventory getSource() {
        return this.sourceInventory;
    }

    @Override
    public ItemStack getItem() {
        this.calledGetItem = true;
        return this.itemStack;
    }

    @Override
    public void setItem(final ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "ItemStack cannot be null. Cancel the event if you want nothing to be transferred.");
        this.itemStack = itemStack.clone();
        this.calledSetItem = true;
    }

    @Override
    public Inventory getDestination() {
        return this.destinationInventory;
    }

    @Override
    public Inventory getInitiator() {
        return this.didSourceInitiate ? this.sourceInventory : this.destinationInventory;
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
        return InventoryMoveItemEvent.getHandlerList();
    }
}

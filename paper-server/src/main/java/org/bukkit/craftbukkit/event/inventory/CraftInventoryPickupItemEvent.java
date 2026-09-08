package org.bukkit.craftbukkit.event.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;

public class CraftInventoryPickupItemEvent extends CraftEvent implements InventoryPickupItemEvent {

    private final Inventory inventory;
    private final Item item;

    private boolean cancelled;

    public CraftInventoryPickupItemEvent(final Inventory inventory, final Item item) {
        this.inventory = inventory;
        this.item = item;
    }

    public CraftInventoryPickupItemEvent(final Container container, final ItemEntity item, final boolean fromHopper) {
        this(CraftInventoryMoveItemEvent.extractInventory(container, fromHopper), (Item) item.getBukkitEntity());
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public Item getItem() {
        return this.item;
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
        return InventoryPickupItemEvent.getHandlerList();
    }
}

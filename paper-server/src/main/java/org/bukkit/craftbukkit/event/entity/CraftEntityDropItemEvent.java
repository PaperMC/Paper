package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDropItemEvent;

public class CraftEntityDropItemEvent extends CraftEntityEvent implements EntityDropItemEvent {

    private final Item drop;
    private boolean cancelled;

    public CraftEntityDropItemEvent(final Entity entity, final Item drop) {
        super(entity);
        this.drop = drop;
    }

    public CraftEntityDropItemEvent(final net.minecraft.world.entity.Entity entity, final ItemEntity drop) {
        this(entity.getBukkitEntity(), (Item) drop.getBukkitEntity());
    }

    @Override
    public Item getItemDrop() {
        return this.drop;
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
        return EntityDropItemEvent.getHandlerList();
    }
}

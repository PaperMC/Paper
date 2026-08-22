package org.bukkit.craftbukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemDespawnEvent;

public class CraftItemDespawnEvent extends CraftEntityEvent implements ItemDespawnEvent {

    private final Location location;
    private boolean cancelled;

    public CraftItemDespawnEvent(final Item despawnee, final Location location) {
        super(despawnee);
        this.location = location;
    }

    @Override
    public Item getEntity() {
        return (Item) this.entity;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
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
        return ItemDespawnEvent.getHandlerList();
    }
}

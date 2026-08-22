package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemMergeEvent;

public class CraftItemMergeEvent extends CraftEntityEvent implements ItemMergeEvent {

    private final Item target;
    private boolean cancelled;

    public CraftItemMergeEvent(final Item item, final Item target) {
        super(item);
        this.target = target;
    }

    @Override
    public Item getEntity() {
        return (Item) this.entity;
    }

    @Override
    public Item getTarget() {
        return this.target;
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
        return ItemMergeEvent.getHandlerList();
    }
}

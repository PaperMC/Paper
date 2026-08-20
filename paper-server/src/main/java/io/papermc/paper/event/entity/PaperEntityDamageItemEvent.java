package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperEntityDamageItemEvent extends CraftEntityEvent implements EntityDamageItemEvent {

    private final ItemStack item;
    private int damage;

    private boolean cancelled;

    public PaperEntityDamageItemEvent(final Entity entity, final ItemStack item, final int damage) {
        super(entity);
        this.item = item;
        this.damage = damage;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(final int damage) {
        this.damage = damage;
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
        return EntityDamageItemEvent.getHandlerList();
    }
}

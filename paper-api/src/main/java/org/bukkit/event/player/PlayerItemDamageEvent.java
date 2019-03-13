package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an item used by the player takes durability damage as a result of
 * being used.
 */
public class PlayerItemDamageEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;
    private int damage;
    private boolean cancelled = false;

    public PlayerItemDamageEvent(@NotNull Player player, @NotNull ItemStack what, int damage) {
        super(player);
        this.item = what;
        this.damage = damage;
    }

    /**
     * Gets the item being damaged.
     *
     * @return the item
     */
    @NotNull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the amount of durability damage this item will be taking.
     *
     * @return durability change
     */
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

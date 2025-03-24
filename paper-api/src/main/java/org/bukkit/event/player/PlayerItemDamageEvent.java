package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an item used by the player takes durability damage as a result of
 * being used.
 */
public class PlayerItemDamageEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack item;
    private final int originalDamage;
    private int damage;
    private boolean cancelled = false;

    @Deprecated(forRemoval = true)
    public PlayerItemDamageEvent(@NotNull Player player, @NotNull ItemStack what, int damage) {
        // Paper start - Add pre-reduction damage
        this(player, what, damage, damage);
    }

    @ApiStatus.Internal
    public PlayerItemDamageEvent(@NotNull Player player, @NotNull ItemStack what, int damage, int originalDamage) {
        super(player);
        this.item = what;
        this.damage = damage;
        this.originalDamage = originalDamage;
        // Paper end
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

    // Paper start - Add pre-reduction damage
    /**
     * Gets the amount of durability damage this item would have taken before
     * the Unbreaking reduction. If the item has no Unbreaking level then
     * this value will be the same as the {@link #getDamage()} value.
     *
     * @return pre-reduction damage amount
     */
    public int getOriginalDamage() {
        return originalDamage;
    }
    // Paper end

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

package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player shoots a projectile.
 * <p>
 * Notably this event is not called for arrows as the player does not launch them, rather shoots them with the help
 * of a bow or crossbow. A plugin may listen to {@link EntityShootBowEvent}
 * for these actions instead.
 */
@NullMarked
public interface PlayerLaunchProjectileEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the projectile which will be launched by this event
     *
     * @return the launched projectile
     */
    Projectile getProjectile();

    /**
     * Get the ItemStack used to fire the projectile
     *
     * @return The ItemStack used
     */
    ItemStack getItemStack();

    /**
     * Get whether to consume the ItemStack or not
     *
     * @return {@code true} to consume
     */
    boolean shouldConsume();

    /**
     * Set whether to consume the ItemStack or not
     *
     * @param consumeItem {@code true} to consume
     */
    void setShouldConsume(boolean consumeItem);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

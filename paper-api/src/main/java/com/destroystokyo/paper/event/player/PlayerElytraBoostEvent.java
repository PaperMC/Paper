package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Firework;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Fired when a player boosts elytra flight with a firework
 */
public interface PlayerElytraBoostEvent extends PlayerEventNew, Cancellable {

    /**
     * Get the firework itemstack used
     *
     * @return ItemStack of firework
     */
    ItemStack getItemStack();

    /**
     * Get the firework entity that was spawned
     *
     * @return Firework entity
     */
    Firework getFirework();

    /**
     * Get whether to consume the firework or not
     *
     * @return {@code true} to consume
     */
    boolean shouldConsume();

    /**
     * Set whether to consume the firework or not
     *
     * @param consume {@code true} to consume
     */
    void setShouldConsume(boolean consume);

    /**
     * Gets the hand holding the firework used for boosting this player.
     *
     * @return interaction hand
     */
    EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

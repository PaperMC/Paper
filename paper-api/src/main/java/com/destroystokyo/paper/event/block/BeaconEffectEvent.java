package com.destroystokyo.paper.event.block;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.potion.PotionEffect;

/**
 * Called when a beacon effect is being applied to a player.
 */
public interface BeaconEffectEvent extends BlockEvent, Cancellable {

    /**
     * Gets the potion effect being applied.
     *
     * @return Potion effect
     */
    PotionEffect getEffect();

    /**
     * Sets the potion effect that will be applied.
     *
     * @param effect Potion effect
     */
    void setEffect(PotionEffect effect);

    /**
     * Gets the player who the potion effect is being applied to.
     *
     * @return Affected player
     */
    Player getPlayer();

    /**
     * Gets whether the effect is a primary beacon effect.
     *
     * @return {@code true} if this event represents a primary effect
     */
    boolean isPrimary();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

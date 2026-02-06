package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player's experience cooldown changes.
 */
public interface PlayerExpCooldownChangeEvent extends PlayerEvent {

    /**
     * Gets the reason for the change.
     *
     * @return The reason for the change
     */
    ChangeReason getReason();

    /**
     * Gets the new cooldown for the player.
     *
     * @return The new cooldown
     * @see Player#getExpCooldown()
     */
    int getNewCooldown();

    /**
     * Sets the new cooldown for the player.
     *
     * @param newCooldown The new cooldown to set
     * @see Player#setExpCooldown(int)
     */
    void setNewCooldown(int newCooldown);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum ChangeReason {

        /**
         * The cooldown was set by picking up an experience orb.
         */
        PICKUP_ORB,
        /**
         * The cooldown was set by a plugin.
         *
         * @see Player#setExpCooldown(int)
         */
        PLUGIN
    }
}

package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player's experience cooldown changes.
 */
@ApiStatus.Experimental
public class PlayerExpCooldownChangeEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private int newCooldown;
    private final ChangeReason reason;

    public PlayerExpCooldownChangeEvent(@NotNull final Player player, int newcooldown, @NotNull ChangeReason reason) {
        super(player);
        this.newCooldown = newcooldown;
        this.reason = reason;
    }

    /**
     * Gets the reason for the change.
     *
     * @return The reason for the change
     */
    @NotNull
    public ChangeReason getReason() {
        return reason;
    }

    /**
     * Gets the new cooldown for the player.
     *
     * @return The new cooldown
     * @see Player#getExpCooldown()
     */
    public int getNewCooldown() {
        return newCooldown;
    }

    /**
     * Sets the new cooldown for the player.
     *
     * @param newCooldown The new cooldown to set
     * @see Player#setExpCooldown(int)
     */
    public void setNewCooldown(int newCooldown) {
        this.newCooldown = newCooldown;
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

    public enum ChangeReason {

        /**
         * The cooldown was set by picking up an experience orb.
         */
        PICKUP_ORB,
        /**
         * The cooldown was set by a plugin.
         *
         * @see Player#setExpCooldown(int)
         */
        PLUGIN;
    }
}

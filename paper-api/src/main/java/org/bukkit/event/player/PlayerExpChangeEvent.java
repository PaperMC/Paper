package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a players experience changes naturally
 */
public class PlayerExpChangeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private int exp;

    public PlayerExpChangeEvent(final Player player, final int expAmount) {
         super(player);
         exp = expAmount;
    }

    /**
     * Get the amount of experience the player will receive
     *
     * @return The amount of experience
     */
    public int getAmount() {
        return exp;
    }

    /**
     * Set the amount of experience the player will receive
     *
     * @param amount The amount of experience to set
     */
    public void setAmount(int amount) {
        exp = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

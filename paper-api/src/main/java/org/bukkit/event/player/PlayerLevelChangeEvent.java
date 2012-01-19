package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a players level changes
 */
public class PlayerLevelChangeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private int oldLevel;
    private int newLevel;

    public PlayerLevelChangeEvent(Player player, int oldLevel, int newLevel) {
         super(Type.PLAYER_LEVEL_CHANGE, player);
         this.oldLevel = oldLevel;
         this.newLevel = newLevel;
    }

    /**
     * Gets the old level of the player
     *
     * @return The old level of the player
     */
    public int getOldLevel() {
        return oldLevel;
    }

    /**
     * Gets the new level of the player
     *
     * @return The new (current) level of the player
     */
    public int getNewLevel() {
        return newLevel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

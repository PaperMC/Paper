package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player joins a server
 */
public class PlayerJoinEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private String joinMessage;

    public PlayerJoinEvent(final Player playerJoined, final String joinMessage) {
        super(playerJoined);
        this.joinMessage = joinMessage;
    }

    /**
     * Gets the join message to send to all online players
     *
     * @return string join message
     */
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message
     */
    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

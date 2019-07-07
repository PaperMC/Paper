package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player joins a server
 */
public class PlayerJoinEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private String joinMessage;

    public PlayerJoinEvent(@NotNull final Player playerJoined, @Nullable final String joinMessage) {
        super(playerJoined);
        this.joinMessage = joinMessage;
    }

    /**
     * Gets the join message to send to all online players
     *
     * @return string join message. Can be null
     */
    @Nullable
    public String getJoinMessage() {
        return joinMessage;
    }

    /**
     * Sets the join message to send to all online players
     *
     * @param joinMessage join message. If null, no message will be sent
     */
    public void setJoinMessage(@Nullable String joinMessage) {
        this.joinMessage = joinMessage;
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

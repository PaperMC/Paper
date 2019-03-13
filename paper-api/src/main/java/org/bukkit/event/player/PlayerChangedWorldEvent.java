package org.bukkit.event.player;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player switches to another world.
 */
public class PlayerChangedWorldEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final World from;

    public PlayerChangedWorldEvent(@NotNull final Player player, @NotNull final World from) {
        super(player);
        this.from = from;
    }

    /**
     * Gets the world the player is switching from.
     *
     * @return  player's previous world
     */
    @NotNull
    public World getFrom() {
        return from;
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

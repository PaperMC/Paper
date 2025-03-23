package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player related event
 */
public abstract class PlayerEvent extends Event {

    protected Player player;

    protected PlayerEvent(@NotNull final Player player) {
        this.player = player;
    }

    protected PlayerEvent(@NotNull final Player player, boolean async) {
        super(async);
        this.player = player;
    }

    /**
     * Returns the player involved in this event
     *
     * @return Player who is involved in this event
     */
    @NotNull
    public final Player getPlayer() {
        return this.player;
    }
}

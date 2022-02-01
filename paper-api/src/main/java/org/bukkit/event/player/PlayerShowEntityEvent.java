package org.bukkit.event.player;

import org.bukkit.Warning;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a hidden entity is shown to a player.
 *
 * This event is only called when the entity's visibility status is actually
 * changed.
 *
 * This event is called regardless of whether the entity was within tracking
 * range.
 *
 * @see Player#showEntity(org.bukkit.plugin.Plugin, org.bukkit.entity.Entity)
 * @deprecated draft API
 */
@Deprecated
@Warning(false)
public class PlayerShowEntityEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;

    public PlayerShowEntityEvent(@NotNull Player who, @NotNull Entity entity) {
        super(who);
        this.entity = entity;
    }

    /**
     * Gets the entity which has been shown to the player.
     *
     * @return the shown entity
     */
    @NotNull
    public Entity getEntity() {
        return entity;
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

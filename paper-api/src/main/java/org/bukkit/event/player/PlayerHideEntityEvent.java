package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a visible entity is hidden from a player.
 * <br>
 * This event is only called when the entity's visibility status is actually
 * changed.
 * <br>
 * This event is called regardless of if the entity was within tracking range.
 *
 * @see Player#hideEntity(org.bukkit.plugin.Plugin, org.bukkit.entity.Entity)
 */
public class PlayerHideEntityEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity entity;

    @ApiStatus.Internal
    public PlayerHideEntityEvent(@NotNull Player player, @NotNull Entity entity) {
        super(player);
        this.entity = entity;
    }

    /**
     * Gets the entity which has been hidden from the player.
     *
     * @return the hidden entity
     */
    @NotNull
    public Entity getEntity() {
        return this.entity;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

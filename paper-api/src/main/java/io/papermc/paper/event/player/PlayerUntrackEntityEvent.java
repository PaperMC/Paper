package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Is called when a {@link Player} untracks an {@link Entity}.
 * <p>
 * Adding or removing entities from the world at the point in time this event is called is completely
 * unsupported and should be avoided.
 */
@NullMarked
public class PlayerUntrackEntityEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity entity;

    @ApiStatus.Internal
    public PlayerUntrackEntityEvent(final Player player, final Entity entity) {
        super(player);
        this.entity = entity;
    }

    /**
     * Gets the entity that will be untracked
     *
     * @return the entity untracked
     */
    public Entity getEntity() {
        return this.entity;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}

package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Is called when a {@link Player} tracks an {@link Entity}.
 * <p>
 * If cancelled, entity is not shown to the player and interaction in both directions is not possible.
 * <p>
 * Adding or removing entities from the world at the point in time this event is called is completely
 * unsupported and should be avoided.
 */
public interface PlayerTrackEntityEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the entity that will be tracked
     *
     * @return the entity tracked
     */
    Entity getEntity();

    /**
     * {@inheritDoc}
     * <p>
     * If cancelled, entity is not shown to the player and interaction in both directions is not possible.
     */
    @Override
    void setCancelled(boolean cancel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

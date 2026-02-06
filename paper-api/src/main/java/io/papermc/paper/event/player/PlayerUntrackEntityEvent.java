package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;

/**
 * Is called when a {@link Player} untracks an {@link Entity}.
 * <p>
 * Adding or removing entities from the world at the point in time this event is called is completely
 * unsupported and should be avoided.
 */
public interface PlayerUntrackEntityEvent extends PlayerEventNew {

    /**
     * Gets the entity that will be untracked
     *
     * @return the entity untracked
     */
    Entity getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

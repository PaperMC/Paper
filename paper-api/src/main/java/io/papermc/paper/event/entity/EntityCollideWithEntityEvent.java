package io.papermc.paper.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Fired when two entities collide with each other.
 * If cancelled, the entities won't get pushed away from each other.
 * <p>
 * Note that even if cancelled, the client may still run its own collision unless
 * disabled via player teams.
 */
public interface EntityCollideWithEntityEvent extends Event, Cancellable {

    /**
     * Returns the entities involved in this event
     *
     * @return entities that are involved in this event
     */
    List<Entity> getEntities();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

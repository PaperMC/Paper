package io.papermc.paper.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Is called when an entity sits down or stands up.
 */
public interface EntityToggleSitEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the new sitting state that the entity will change to.
     *
     * @return If it's going to sit or not.
     */
    boolean getSittingState();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

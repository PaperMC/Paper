package io.papermc.paper.event.entity;

import org.bukkit.entity.Shulker;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Fired when a shulker duplicates itself by spawning a new shulker.
 * <p>
 * The event is fired prior to the newly created shulker, accessible via {@link #getEntity()}, being added to the world.
 */
public interface ShulkerDuplicateEvent extends EntityEventNew, Cancellable {

    /**
     * Provides the newly created shulker, which did not exist prior to the duplication.
     * At the point of this event, said shulker is not part of the world yet.
     *
     * @return the newly duplicated shulker.
     */
    @Override
    Shulker getEntity();

    /**
     * Provides the "parent" of the freshly created shulker.
     * The parent shulker is the one that initiated the duplication.
     *
     * @return the previously existing shulker which duplicated.
     */
    Shulker getParent();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

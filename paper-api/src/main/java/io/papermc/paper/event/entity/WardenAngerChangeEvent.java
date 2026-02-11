package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Warden;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.checkerframework.common.value.qual.IntRange;

/**
 * Called when a Warden's anger level has changed due to another entity.
 * <p>
 * If the event is cancelled, the warden's anger level will not change.
 */
public interface WardenAngerChangeEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the entity which triggered this anger update.
     *
     * @return triggering entity
     */
    Entity getTarget();

    /**
     * Gets the old anger level.
     *
     * @return old anger level
     * @see Warden#getAnger(Entity)
     */
    @IntRange(from = 0, to = 150) int getOldAnger();

    /**
     * Gets the new anger level resulting from this event.
     *
     * @return new anger level
     * @see Warden#getAnger(Entity)
     */
    @IntRange(from = 0, to = 150) int getNewAnger();

    /**
     * Sets the new anger level resulting from this event.
     * <p>
     * The anger of a warden is capped at 150.
     *
     * @param newAnger the new anger level, max 150
     * @throws IllegalArgumentException if newAnger is greater than 150
     * @see Warden#setAnger(Entity, int)
     */
    void setNewAnger(@IntRange(from = 0, to = 150) int newAnger);

    @Override
    Warden getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

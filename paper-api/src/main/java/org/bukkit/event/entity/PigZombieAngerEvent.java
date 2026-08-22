package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a Zombified piglin is angered by another entity.
 * <p>
 * If the event is cancelled, the zombified piglin will not be angered.
 */
public interface PigZombieAngerEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the entity (if any) which triggered this anger update.
     *
     * @return triggering entity, or {@code null}
     */
    @Nullable Entity getTarget();

    /**
     * Gets the new anger resulting from this event.
     *
     * @return new anger
     * @see PigZombie#getAnger()
     */
    int getNewAnger();

    /**
     * Sets the new anger resulting from this event.
     *
     * @param newAnger the new anger
     * @see PigZombie#setAnger(int)
     */
    void setNewAnger(int newAnger);

    @Override
    PigZombie getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

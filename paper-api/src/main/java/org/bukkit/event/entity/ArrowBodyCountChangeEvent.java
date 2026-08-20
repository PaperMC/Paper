package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an arrow enters or exists an entity's body.
 */
public interface ArrowBodyCountChangeEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Whether the event was called because the entity was reset.
     *
     * @return was reset
     */
    boolean isReset();

    /**
     * Gets the old amount of arrows in the entity's body.
     *
     * @return amount of arrows
     */
    int getOldAmount();

    /**
     * Get the new amount of arrows in the entity's body.
     *
     * @return amount of arrows
     */
    int getNewAmount();

    /**
     * Sets the final amount of arrows in the entity's body.
     *
     * @param newAmount amount of arrows
     */
    void setNewAmount(int newAmount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

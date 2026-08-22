package org.bukkit.event.entity;

import org.bukkit.entity.Animals;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity enters love mode.
 * <p>
 * This can be cancelled but the item will still be consumed that was used to
 * make the entity enter into love mode.
 */
public interface EntityEnterLoveModeEvent extends EntityEvent, Cancellable {

    /**
     * Gets the animal that is entering love mode.
     *
     * @return The animal that is entering love mode
     */
    @Override
    Animals getEntity();

    /**
     * Gets the Human Entity that caused the animal to enter love mode.
     *
     * @return The Human entity that caused the animal to enter love mode, or
     * {@code null} if there wasn't one.
     */
    @Nullable HumanEntity getHumanEntity();

    /**
     * Gets the amount of ticks that the animal will fall in love for.
     *
     * @return The amount of ticks that the animal will fall in love for
     */
    int getTicksInLove();

    /**
     * Sets the amount of ticks that the animal will fall in love for.
     *
     * @param ticksInLove The amount of ticks that the animal will fall in love
     * for
     */
    void setTicksInLove(int ticksInLove);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

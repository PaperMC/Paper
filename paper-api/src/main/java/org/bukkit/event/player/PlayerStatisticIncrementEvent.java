package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player statistic is incremented.
 * <p>
 * This event is not called for some high frequency statistics, e.g. movement
 * based statistics.
 */
public interface PlayerStatisticIncrementEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the statistic that is being incremented.
     *
     * @return the incremented statistic
     */
    Statistic getStatistic();

    /**
     * Gets the previous value of the statistic.
     *
     * @return the previous value of the statistic
     */
    int getPreviousValue();

    /**
     * Gets the new value of the statistic.
     *
     * @return the new value of the statistic
     */
    int getNewValue();

    /**
     * Gets the EntityType if {@link #getStatistic()} is an
     * entity statistic otherwise returns {@code null}.
     *
     * @return the EntityType of the statistic
     */
    @Nullable EntityType getEntityType();

    /**
     * Gets the Material if {@link #getStatistic()} is a block
     * or item statistic otherwise returns {@code null}.
     *
     * @return the Material of the statistic
     */
    @Nullable Material getMaterial();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

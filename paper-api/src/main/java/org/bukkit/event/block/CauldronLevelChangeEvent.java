package org.bukkit.event.block;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;
import org.jspecify.annotations.Nullable;

public interface CauldronLevelChangeEvent extends BlockEventNew, Cancellable {

    /**
     * Get entity which did this. May be {@code null}.
     *
     * @return acting entity
     */
    @Nullable Entity getEntity();

    // todo javadocs?
    ChangeReason getReason();

    /**
     * Gets the new state of the cauldron.
     *
     * @return The block state of the block that will be changed
     */
    BlockState getNewState();

    /**
     * Gets the old level of the cauldron.
     *
     * @return old level
     * @see #getBlock()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated(since = "1.17")
    @IntRange(from = 0, to = 3) int getOldLevel();

    /**
     * Gets the new level of the cauldron.
     *
     * @return new level
     * @see #getNewState()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated(since = "1.17")
    @IntRange(from = 0, to = 3) int getNewLevel();

    /**
     * Sets the new level of the cauldron.
     *
     * @param newLevel new level
     * @see #getNewState()
     * @deprecated not all cauldron contents are Levelled
     */
    @Deprecated(since = "1.17")
    void setNewLevel(@IntRange(from = 0, to = 3) int newLevel);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum ChangeReason {
        /**
         * Player emptying the cauldron by filling their bucket.
         */
        BUCKET_FILL,
        /**
         * Player filling the cauldron by emptying their bucket.
         */
        BUCKET_EMPTY,
        /**
         * Player emptying the cauldron by filling their bottle.
         */
        BOTTLE_FILL,
        /**
         * Player filling the cauldron by emptying their bottle.
         */
        BOTTLE_EMPTY,
        /**
         * Player cleaning their banner.
         */
        BANNER_WASH,
        /**
         * Player cleaning their armor.
         */
        ARMOR_WASH,
        /**
         * Player cleaning a shulker box.
         */
        SHULKER_WASH,
        /**
         * Entity being extinguished.
         */
        EXTINGUISH,
        /**
         * Evaporating due to biome dryness.
         */
        EVAPORATE,
        /**
         * Filling due to natural fluid sources, e.g. rain or dripstone.
         */
        NATURAL_FILL,
        /**
         * Unknown.
         */
        UNKNOWN
    }
}

package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.Warning;
import org.bukkit.entity.Fish;
import org.bukkit.inventory.ItemStack;

/**
 * This event is called whenever a player attempts to put a fish in a bucket.
 *
 * @deprecated Use the more generic {@link PlayerBucketEntityEvent}
 */
@Warning
@Deprecated(since = "1.16.5")
public interface PlayerBucketFishEvent extends PlayerBucketEntityEvent {

    /**
     * Gets the fish involved with this event.
     *
     * @return The fish involved with this event
     */
    @Override
    Fish getEntity();

    /**
     * Gets the bucket used.
     * <br>
     * This refers to the bucket clicked with, ie {@link Material#WATER_BUCKET}.
     *
     * @return The used bucket
     * @deprecated Use {@link #getOriginalBucket()}
     */
    @Deprecated(since = "1.16.5")
    default ItemStack getWaterBucket() {
        return this.getOriginalBucket();
    }

    /**
     * Gets the bucket that the fish will be put into.
     * <br>
     * This refers to the bucket with the fish, ie
     * {@link Material#PUFFERFISH_BUCKET}.
     *
     * @return The bucket that the fish will be put into
     * @deprecated Use {@link #getEntityBucket()}
     */
    @Deprecated(since = "1.16.5")
    default ItemStack getFishBucket() {
        return this.getEntityBucket();
    }
}

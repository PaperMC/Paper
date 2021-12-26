package io.papermc.paper.entity;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an entity that can be bucketed.
 */
@NullMarked
public interface Bucketable extends Entity {

    /**
     * Gets if this entity originated from a bucket.
     *
     * @return originated from bucket
     */
    boolean isFromBucket();

    /**
     * Sets if this entity originated from a bucket.
     *
     * @param fromBucket is from a bucket
     */
    void setFromBucket(boolean fromBucket);

    /**
     * Gets the base itemstack of this entity in a bucket form.
     *
     * @return bucket form
     */
    ItemStack getBaseBucketItem();

    /**
     * Gets the sound that is played when this entity
     * is picked up in a bucket.
     * @return bucket pickup sound
     */
    Sound getPickupSound();
}

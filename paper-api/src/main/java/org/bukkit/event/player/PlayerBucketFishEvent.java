package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.Warning;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called whenever a player attempts to put a fish in a bucket.
 *
 * @deprecated Use the more generic {@link PlayerBucketEntityEvent}
 */
@Deprecated(since = "1.16.5")
@Warning
public class PlayerBucketFishEvent extends PlayerBucketEntityEvent {

    @ApiStatus.Internal
    public PlayerBucketFishEvent(@NotNull Player player, @NotNull Fish fish, @NotNull ItemStack waterBucket, @NotNull ItemStack fishBucket, @NotNull EquipmentSlot hand) {
        super(player, fish, waterBucket, fishBucket, hand);
    }

    /**
     * Gets the fish involved with this event.
     *
     * @return The fish involved with this event
     */
    @NotNull
    @Override
    public Fish getEntity() {
        return (Fish) this.entity;
    }

    /**
     * Gets the bucket used.
     * <br>
     * This refers to the bucket clicked with, ie {@link Material#WATER_BUCKET}.
     *
     * @return The used bucket
     * @deprecated Use {@link #getOriginalBucket()}
     */
    @NotNull
    @Deprecated(since = "1.16.5")
    public ItemStack getWaterBucket() {
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
    @NotNull
    @Deprecated(since = "1.16.5")
    public ItemStack getFishBucket() {
        return this.getEntityBucket();
    }
}

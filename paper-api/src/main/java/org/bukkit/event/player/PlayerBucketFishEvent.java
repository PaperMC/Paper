package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called whenever a player attempts to put a fish in a bucket.
 */
public class PlayerBucketFishEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancalled;
    private final Fish fish;
    private final ItemStack waterBucket;
    private final ItemStack fishBucket;

    public PlayerBucketFishEvent(@NotNull Player player, @NotNull Fish fish, @NotNull ItemStack waterBucket, @NotNull ItemStack fishBucket) {
        super(player);
        this.fish = fish;
        this.waterBucket = waterBucket;
        this.fishBucket = fishBucket;
    }

    /**
     * Gets the fish involved with this event.
     *
     * @return The fish involved with this event
     */
    @NotNull
    public Fish getEntity() {
        return fish;
    }

    /**
     * Gets the bucket used.
     *
     * This refers to the bucket clicked with, ie {@link Material#WATER}.
     *
     * @return The used bucket
     */
    @NotNull
    public ItemStack getWaterBucket() {
        return fishBucket;
    }

    /**
     * Gets the bucket that the fish will be put into.
     *
     * This refers to the bucket with the fish, ie
     * {@link Material#PUFFERFISH_BUCKET}.
     *
     * @return The bucket that the fish will be put into
     */
    @NotNull
    public ItemStack getFishBucket() {
        return fishBucket;
    }

    @Override
    public boolean isCancelled() {
        return cancalled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancalled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

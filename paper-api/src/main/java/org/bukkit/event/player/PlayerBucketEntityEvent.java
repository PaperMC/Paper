package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * This event is called whenever a player captures an entity in a bucket.
 */
public interface PlayerBucketEntityEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the {@link Entity} being put into the bucket.
     *
     * @return The {@link Entity} being put into the bucket
     */
    Entity getEntity();

    /**
     * Gets the bucket used to capture the {@link Entity}.
     * <br>
     * This refers to the bucket clicked with, eg {@link Material#WATER_BUCKET}.
     *
     * @return The used bucket
     */
    ItemStack getOriginalBucket();

    /**
     * Gets the bucket that the {@link Entity} will be put into.
     * <br>
     * This refers to the bucket with the entity, eg
     * {@link Material#PUFFERFISH_BUCKET}.
     *
     * @return The bucket that the {@link Entity} will be put into
     */
    ItemStack getEntityBucket();

    /**
     * Get the hand that was used to bucket the entity.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

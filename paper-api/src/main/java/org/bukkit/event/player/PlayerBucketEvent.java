package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player interacts with a Bucket
 */
public interface PlayerBucketEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the block involved in this event.
     *
     * @return The Block which block is involved in this event
     */
    Block getBlock();

    /**
     * Return the block clicked
     *
     * @return the clicked block
     */
    Block getBlockClicked();

    /**
     * Get the face on the clicked block
     *
     * @return the clicked face
     */
    BlockFace getBlockFace();

    /**
     * Returns the bucket used in this event
     *
     * @return the used bucket
     */
    Material getBucket();

    /**
     * Get the hand that was used in this event.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    /**
     * Get the resulting item in hand after the bucket event
     *
     * @return ItemStack hold in hand after the event.
     */
    @Nullable ItemStack getItemStack();

    /**
     * Set the item in hand after the event
     *
     * @param itemStack the new held ItemStack after the bucket event.
     */
    void setItemStack(@Nullable ItemStack itemStack);
}

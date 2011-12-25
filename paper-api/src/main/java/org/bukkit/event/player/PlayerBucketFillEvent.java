package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player fills a bucket
 */
@SuppressWarnings("serial")
public class PlayerBucketFillEvent extends PlayerBucketEvent {
    public PlayerBucketFillEvent(Player who, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
        super(Type.PLAYER_BUCKET_FILL, who, blockClicked, blockFace, bucket, itemInHand);
    }
}

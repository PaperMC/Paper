package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerBucketFillEvent extends PlayerEvent {
    ItemStack itemStack;
    boolean cancelled = false;
    Block blockClicked;
    BlockFace blockFace;
    Material bucket;

    public PlayerBucketFillEvent(Player who, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand) {
        super(Type.PLAYER_BUCKET_FILL, who);
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.itemStack = itemInHand;
        this.bucket = bucket;
    }

    public Material getBucket() {
        return bucket;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Block getBlockClicked() {
        return blockClicked;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}

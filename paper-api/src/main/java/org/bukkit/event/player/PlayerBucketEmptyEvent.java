package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player empties a bucket
 */
public class PlayerBucketEmptyEvent extends PlayerBucketEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    @Deprecated(since = "1.14.4", forRemoval = true)
    public PlayerBucketEmptyEvent(@NotNull final Player player, @NotNull final Block blockClicked, @NotNull final BlockFace blockFace, @NotNull final Material bucket, @NotNull final ItemStack itemInHand) {
        super(player, blockClicked, blockFace, bucket, itemInHand);
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.2", forRemoval = true)
    public PlayerBucketEmptyEvent(@NotNull final Player player, @NotNull final Block block, @NotNull final Block blockClicked, @NotNull final BlockFace blockFace, @NotNull final Material bucket, @NotNull final ItemStack itemInHand) {
        super(player, block, blockClicked, blockFace, bucket, itemInHand);
    }

    @ApiStatus.Internal
    public PlayerBucketEmptyEvent(@NotNull final Player player, @NotNull final Block block, @NotNull final Block blockClicked, @NotNull final BlockFace blockFace, @NotNull final Material bucket, @NotNull final ItemStack itemInHand, @NotNull final EquipmentSlot hand) {
        super(player, block, blockClicked, blockFace, bucket, itemInHand, hand);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

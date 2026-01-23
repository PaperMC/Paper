package org.bukkit.craftbukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerBucketEmptyEvent extends CraftPlayerBucketEvent implements PlayerBucketEmptyEvent {

    public CraftPlayerBucketEmptyEvent(final Player player, final Block block, final Block blockClicked, final BlockFace blockFace, final Material bucket, final ItemStack itemInHand, final EquipmentSlot hand) {
        super(player, block, blockClicked, blockFace, bucket, itemInHand, hand);
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerBucketEmptyEvent.getHandlerList();
    }
}

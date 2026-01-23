package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerBucketFishEvent extends CraftPlayerBucketEntityEvent implements PlayerBucketFishEvent {

    public CraftPlayerBucketFishEvent(final Player player, final Fish fish, final ItemStack waterBucket, final ItemStack fishBucket, final EquipmentSlot hand) {
        super(player, fish, waterBucket, fishBucket, hand);
    }

    @Override
    public Fish getEntity() {
        return (Fish) super.getEntity();
    }
}

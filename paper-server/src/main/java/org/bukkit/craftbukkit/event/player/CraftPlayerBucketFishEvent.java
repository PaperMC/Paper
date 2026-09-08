package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.entity.Fish;
import org.bukkit.event.player.PlayerBucketFishEvent;

public class CraftPlayerBucketFishEvent extends CraftPlayerBucketEntityEvent implements PlayerBucketFishEvent {

    public CraftPlayerBucketFishEvent(
        final net.minecraft.world.entity.player.Player player,
        final LivingEntity fish,
        final net.minecraft.world.item.ItemStack originalBucket,
        final net.minecraft.world.item.ItemStack entityBucket,
        final InteractionHand hand
    ) {
        super(player, fish, originalBucket, entityBucket, hand);
    }

    @Override
    public Fish getEntity() {
        return (Fish) super.getEntity();
    }
}

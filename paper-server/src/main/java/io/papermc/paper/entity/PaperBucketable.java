package io.papermc.paper.entity;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public interface PaperBucketable extends Bucketable {

    net.minecraft.world.entity.animal.Bucketable getHandle();

    @Override
    default boolean isFromBucket() {
        return this.getHandle().fromBucket();
    }

    @Override
    default void setFromBucket(boolean fromBucket) {
        this.getHandle().setFromBucket(fromBucket);
    }

    @Override
    default ItemStack getBaseBucketItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getBucketItemStack());
    }

    @Override
    default Sound getPickupSound() {
        return CraftSound.minecraftToBukkit(this.getHandle().getPickupSound());
    }
}

package net.minecraft.server;

import org.bukkit.event.player.PlayerFishEvent; // CraftBukkit

public class ItemFishingRod extends Item {

    public ItemFishingRod(int i) {
        super(i);
        this.setMaxDurability(64);
        this.d(1);
        this.a(CreativeModeTab.i);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (entityhuman.hookedFish != null) {
            int i = entityhuman.hookedFish.c();

            itemstack.damage(i, entityhuman);
            entityhuman.bE();
        } else {
            // CraftBukkit start
            PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), null, PlayerFishEvent.State.FISHING);
            world.getServer().getPluginManager().callEvent(playerFishEvent);

            if (playerFishEvent.isCancelled()) {
                return itemstack;
            }
            // CraftBukkit end

            world.makeSound(entityhuman, "random.bow", 0.5F, 0.4F / (d.nextFloat() * 0.4F + 0.8F));
            if (!world.isStatic) {
                world.addEntity(new EntityFishingHook(world, entityhuman));
            }

            entityhuman.bE();
        }

        return itemstack;
    }
}

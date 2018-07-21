package net.minecraft.server;

public class ItemEgg extends Item {

    public ItemEgg(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        // world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (ItemEgg.RANDOM.nextFloat() * 0.4F + 0.8F)); // CraftBukkit - moved down
        if (!world.isClientSide) {
            EntityEgg entityegg = new EntityEgg(world, entityhuman);

            entityegg.setItem(itemstack);
            entityegg.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F, 1.5F, 1.0F);
            // Paper start
            com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent event = new com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack), (org.bukkit.entity.Projectile) entityegg.getBukkitEntity());
            if (event.callEvent() && world.addEntity(entityegg)) {
                if (event.shouldConsume() && !entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                } else if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }

                world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (Entity.SHARED_RANDOM.nextFloat() * 0.4F + 0.8F));
                entityhuman.b(StatisticList.ITEM_USED.b(this));
            } else {
                if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }
                return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.FAIL, itemstack);
            }
            // Paper end


        }
        world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (ItemEgg.RANDOM.nextFloat() * 0.4F + 0.8F)); // CraftBukkit - from above

        /* // Paper start - moved up
        entityhuman.b(StatisticList.ITEM_USED.b(this));
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }
        */ // Paper end

        return InteractionResultWrapper.a(itemstack, world.s_());
    }
}

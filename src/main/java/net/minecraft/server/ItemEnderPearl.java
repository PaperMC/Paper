package net.minecraft.server;

public class ItemEnderPearl extends Item {

    public ItemEnderPearl(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        // CraftBukkit start - change order
        if (!world.isClientSide) {
            EntityEnderPearl entityenderpearl = new EntityEnderPearl(world, entityhuman);

            entityenderpearl.setItem(itemstack);
            entityenderpearl.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F, 1.5F, 1.0F);
            // Paper start
                com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent event = new com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack), (org.bukkit.entity.Projectile) entityenderpearl.getBukkitEntity());
            if (event.callEvent() && world.addEntity(entityenderpearl)) {
                if (event.shouldConsume() && !entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                } else if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }

                world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (Entity.SHARED_RANDOM.nextFloat() * 0.4F + 0.8F));
                entityhuman.b(StatisticList.ITEM_USED.b(this));
                entityhuman.getCooldownTracker().setCooldown(this, 20);
            } else {
                // Paper end
                if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }
                return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.FAIL, itemstack);
            }
        }

        // Paper start - moved up
//        world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemEnderPearl.RANDOM.nextFloat() * 0.4F + 0.8F));
//        entityhuman.getCooldownTracker().setCooldown(this, 20);
//        // CraftBukkit end
//
//        entityhuman.b(StatisticList.ITEM_USED.b(this));
//        if (!entityhuman.abilities.canInstantlyBuild) {
//            itemstack.subtract(1);
//        }
        // Paper end - moved up

        return InteractionResultWrapper.a(itemstack, world.s_());
    }
}

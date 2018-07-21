package net.minecraft.server;

public class ItemPotionThrowable extends ItemPotion {

    public ItemPotionThrowable(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!world.isClientSide) {
            EntityPotion entitypotion = new EntityPotion(world, entityhuman);

            entitypotion.setItem(itemstack);
            entitypotion.a(entityhuman, entityhuman.pitch, entityhuman.yaw, -20.0F, 0.5F, 1.0F);
            // Paper start
            com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent event = new com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack), (org.bukkit.entity.Projectile) entitypotion.getBukkitEntity());
            if (event.callEvent() && world.addEntity(entitypotion)) {
                if (event.shouldConsume() && !entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                } else if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }

                entityhuman.b(StatisticList.ITEM_USED.b(this));
            } else {
                if (entityhuman instanceof EntityPlayer) {
                    ((EntityPlayer) entityhuman).getBukkitEntity().updateInventory();
                }
                    return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.FAIL, itemstack);
            }
            // Paper end
        }

        /* // Paper start - moved up
        entityhuman.b(StatisticList.ITEM_USED.b(this));
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }
        */ // Paper end

        return InteractionResultWrapper.a(itemstack, world.s_());
    }
}

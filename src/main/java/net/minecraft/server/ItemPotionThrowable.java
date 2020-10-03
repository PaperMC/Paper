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
            world.addEntity(entitypotion);
        }

        entityhuman.b(StatisticList.ITEM_USED.b(this));
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        return InteractionResultWrapper.a(itemstack, world.s_());
    }
}

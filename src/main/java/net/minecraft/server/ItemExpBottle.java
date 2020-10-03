package net.minecraft.server;

public class ItemExpBottle extends Item {

    public ItemExpBottle(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public boolean e(ItemStack itemstack) {
        return true;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemExpBottle.RANDOM.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            EntityThrownExpBottle entitythrownexpbottle = new EntityThrownExpBottle(world, entityhuman);

            entitythrownexpbottle.setItem(itemstack);
            entitythrownexpbottle.a(entityhuman, entityhuman.pitch, entityhuman.yaw, -20.0F, 0.7F, 1.0F);
            world.addEntity(entitythrownexpbottle);
        }

        entityhuman.b(StatisticList.ITEM_USED.b(this));
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        return InteractionResultWrapper.a(itemstack, world.s_());
    }
}

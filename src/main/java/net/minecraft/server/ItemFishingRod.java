package net.minecraft.server;

public class ItemFishingRod extends Item implements ItemVanishable {

    public ItemFishingRod(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        int i;

        if (entityhuman.hookedFish != null) {
            if (!world.isClientSide) {
                i = entityhuman.hookedFish.b(itemstack);
                itemstack.damage(i, entityhuman, (entityhuman1) -> {
                    entityhuman1.broadcastItemBreak(enumhand);
                });
            }

            world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (ItemFishingRod.RANDOM.nextFloat() * 0.4F + 0.8F));
        } else {
            world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemFishingRod.RANDOM.nextFloat() * 0.4F + 0.8F));
            if (!world.isClientSide) {
                i = EnchantmentManager.c(itemstack);
                int j = EnchantmentManager.b(itemstack);

                world.addEntity(new EntityFishingHook(entityhuman, world, j, i));
            }

            entityhuman.b(StatisticList.ITEM_USED.b(this));
        }

        return InteractionResultWrapper.a(itemstack, world.s_());
    }

    @Override
    public int c() {
        return 1;
    }
}

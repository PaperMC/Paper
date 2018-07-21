package net.minecraft.server;

public class ItemSplashPotion extends ItemPotionThrowable {

    public ItemSplashPotion(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        // Paper start
        InteractionResultWrapper<ItemStack> wrapper = super.a(world, entityhuman, enumhand);
        if (wrapper.getResult() != EnumInteractionResult.FAIL) {
        world.playSound((EntityHuman) null, entityhuman.locX(), entityhuman.locY(), entityhuman.locZ(), SoundEffects.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (ItemSplashPotion.RANDOM.nextFloat() * 0.4F + 0.8F));
        }
        return wrapper;
        // Paper end
    }
}

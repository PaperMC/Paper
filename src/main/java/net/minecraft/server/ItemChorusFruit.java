package net.minecraft.server;

public class ItemChorusFruit extends Item {

    public ItemChorusFruit(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public ItemStack a(ItemStack itemstack, World world, EntityLiving entityliving) {
        ItemStack itemstack1 = super.a(itemstack, world, entityliving);

        if (!world.isClientSide) {
            double d0 = entityliving.locX();
            double d1 = entityliving.locY();
            double d2 = entityliving.locZ();

            for (int i = 0; i < 16; ++i) {
                double d3 = entityliving.locX() + (entityliving.getRandom().nextDouble() - 0.5D) * 16.0D;
                double d4 = MathHelper.a(entityliving.locY() + (double) (entityliving.getRandom().nextInt(16) - 8), 0.0D, (double) (world.getHeight() - 1));
                double d5 = entityliving.locZ() + (entityliving.getRandom().nextDouble() - 0.5D) * 16.0D;

                if (entityliving.isPassenger()) {
                    entityliving.stopRiding();
                }

                if (entityliving.a(d3, d4, d5, true)) {
                    SoundEffect soundeffect = entityliving instanceof EntityFox ? SoundEffects.ENTITY_FOX_TELEPORT : SoundEffects.ITEM_CHORUS_FRUIT_TELEPORT;

                    world.playSound((EntityHuman) null, d0, d1, d2, soundeffect, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityliving.playSound(soundeffect, 1.0F, 1.0F);
                    break;
                }
            }

            if (entityliving instanceof EntityHuman) {
                ((EntityHuman) entityliving).getCooldownTracker().setCooldown(this, 20);
            }
        }

        return itemstack1;
    }
}

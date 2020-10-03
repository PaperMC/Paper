package net.minecraft.server;

public class ItemMilkBucket extends Item {

    public ItemMilkBucket(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public ItemStack a(ItemStack itemstack, World world, EntityLiving entityliving) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityliving;

            CriterionTriggers.z.a(entityplayer, itemstack);
            entityplayer.b(StatisticList.ITEM_USED.b(this));
        }

        if (entityliving instanceof EntityHuman && !((EntityHuman) entityliving).abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        if (!world.isClientSide) {
            entityliving.removeAllEffects(org.bukkit.event.entity.EntityPotionEffectEvent.Cause.MILK); // CraftBukkit
        }

        return itemstack.isEmpty() ? new ItemStack(Items.BUCKET) : itemstack;
    }

    @Override
    public int e_(ItemStack itemstack) {
        return 32;
    }

    @Override
    public EnumAnimation d_(ItemStack itemstack) {
        return EnumAnimation.DRINK;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        return ItemLiquidUtil.a(world, entityhuman, enumhand);
    }
}

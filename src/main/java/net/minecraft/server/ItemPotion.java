package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class ItemPotion extends Item {

    public ItemPotion(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public ItemStack createItemStack() {
        return PotionUtil.a(super.createItemStack(), Potions.WATER);
    }

    @Override
    public ItemStack a(ItemStack itemstack, World world, EntityLiving entityliving) {
        EntityHuman entityhuman = entityliving instanceof EntityHuman ? (EntityHuman) entityliving : null;

        if (entityhuman instanceof EntityPlayer) {
            CriterionTriggers.z.a((EntityPlayer) entityhuman, itemstack);
        }

        if (!world.isClientSide) {
            List<MobEffect> list = PotionUtil.getEffects(itemstack);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                if (mobeffect.getMobEffect().isInstant()) {
                    mobeffect.getMobEffect().applyInstantEffect(entityhuman, entityhuman, entityliving, mobeffect.getAmplifier(), 1.0D);
                } else {
                    entityliving.addEffect(new MobEffect(mobeffect), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.POTION_DRINK); // CraftBukkit
                }
            }
        }

        if (entityhuman != null) {
            entityhuman.b(StatisticList.ITEM_USED.b(this));
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }
        }

        if (entityhuman == null || !entityhuman.abilities.canInstantlyBuild) {
            if (itemstack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (entityhuman != null) {
                entityhuman.inventory.pickup(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return itemstack;
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

    @Override
    public String f(ItemStack itemstack) {
        return PotionUtil.d(itemstack).b(this.getName() + ".effect.");
    }

    @Override
    public boolean e(ItemStack itemstack) {
        return super.e(itemstack) || !PotionUtil.getEffects(itemstack).isEmpty();
    }

    @Override
    public void a(CreativeModeTab creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.a(creativemodetab)) {
            Iterator iterator = IRegistry.POTION.iterator();

            while (iterator.hasNext()) {
                PotionRegistry potionregistry = (PotionRegistry) iterator.next();

                if (potionregistry != Potions.EMPTY) {
                    nonnulllist.add(PotionUtil.a(new ItemStack(this), potionregistry));
                }
            }
        }

    }
}

package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.item.alchemy.PotionUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class CraftThrownPotion extends CraftThrowableProjectile implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, EntityPotion entity) {
        super(server, entity);
    }

    @Override
    public Collection<PotionEffect> getEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffect effect : PotionUtil.getMobEffects(getHandle().getItemRaw())) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().getItemRaw());
    }

    @Override
    public void setItem(ItemStack item) {
        // The ItemStack must not be null.
        Validate.notNull(item, "ItemStack cannot be null.");

        // The ItemStack must be a potion.
        Validate.isTrue(item.getType() == Material.LINGERING_POTION || item.getType() == Material.SPLASH_POTION, "ItemStack must be a lingering or splash potion. This item stack was " + item.getType() + ".");

        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion) entity;
    }

    @Override
    public EntityType getType() {
        return EntityType.SPLASH_POTION;
    }
}

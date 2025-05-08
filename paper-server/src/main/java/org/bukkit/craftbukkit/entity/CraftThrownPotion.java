package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.AbstractThrownPotion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public abstract class CraftThrownPotion extends CraftThrowableProjectile implements ThrownPotion {

    protected CraftThrownPotion(CraftServer server, AbstractThrownPotion entity) {
        super(server, entity);
    }

    @Override
    public AbstractThrownPotion getHandle() {
        return (AbstractThrownPotion) this.entity;
    }

    @Override
    public Collection<PotionEffect> getEffects() {
        ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (MobEffectInstance effect : this.getHandle().getItem().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getAllEffects()) {
            builder.add(CraftPotionUtil.toBukkit(effect));
        }
        return builder.build();
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    @Override
    public void setPotionMeta(org.bukkit.inventory.meta.PotionMeta meta) {
        net.minecraft.world.item.ItemStack item = this.getHandle().getItem();
        CraftItemStack.applyMetaToItem(item, meta);
        this.getHandle().setItem(item); // Reset item
    }

    @Override
    public void splash() {
        this.getHandle().splash(null);
    }
}

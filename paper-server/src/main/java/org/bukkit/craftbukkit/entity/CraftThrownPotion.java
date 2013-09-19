package org.bukkit.craftbukkit.entity;

import java.util.Collection;

import net.minecraft.server.EntityPotion;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

public class CraftThrownPotion extends CraftProjectile implements ThrownPotion {
    public CraftThrownPotion(CraftServer server, EntityPotion entity) {
        super(server, entity);
    }

    // TODO: This one does not handle custom NBT potion effects does it?
    // In that case this method could be said to be misleading or incorrect
    public Collection<PotionEffect> getEffects() {
        return Potion.getBrewer().getEffectsFromDamage(getHandle().getPotionValue());
    }

    public ItemStack getItem() {
        // We run this method once since it will set the item stack if there is none.
        getHandle().getPotionValue();

        return CraftItemStack.asBukkitCopy(getHandle().item);
    }

    public void setItem(ItemStack item) {
        // The ItemStack must not be null.
        Validate.notNull(item, "ItemStack cannot be null.");

        // The ItemStack must be a potion.
        Validate.isTrue(item.getType() == Material.POTION, "ItemStack must be a potion. This item stack was " + item.getType() + ".");

        getHandle().item = CraftItemStack.asNMSCopy(item);
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion) entity;
    }

    @Override
    public String toString() {
        return "CraftThrownPotion";
    }

    public EntityType getType() {
        return EntityType.SPLASH_POTION;
    }
}

package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPotion;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SplashPotion;
import org.bukkit.inventory.ItemStack;

public class CraftSplashPotion extends CraftThrownPotion implements SplashPotion {

    public CraftSplashPotion(CraftServer server, EntityPotion entity) {
        super(server, entity);
    }

    @Override
    public void setItem(ItemStack item) {
        // The ItemStack must not be null.
        Validate.notNull(item, "ItemStack cannot be null.");

        // The ItemStack must be a potion.
        Validate.isTrue(item.getType() == Material.SPLASH_POTION, "ItemStack must be a splash potion. This item stack was " + item.getType() + ".");

        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion) entity;
    }

    @Override
    public String toString() {
        return "CraftSplashPotion";
    }

    @Override
    public EntityType getType() {
        return EntityType.SPLASH_POTION;
    }
}

package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPotion;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.inventory.ItemStack;

public class CraftLingeringPotion extends CraftThrownPotion implements LingeringPotion {

    public CraftLingeringPotion(CraftServer server, EntityPotion entity) {
        super(server, entity);
    }

    public void setItem(ItemStack item) {
        // The ItemStack must not be null.
        Validate.notNull(item, "ItemStack cannot be null.");

        // The ItemStack must be a potion.
        Validate.isTrue(item.getType() == Material.LINGERING_POTION, "ItemStack must be a lingering potion. This item stack was " + item.getType() + ".");

        getHandle().setItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion) entity;
    }

    @Override
    public String toString() {
        return "CraftLingeringPotion";
    }

    @Override
    public EntityType getType() {
        return EntityType.LINGERING_POTION;
    }
}

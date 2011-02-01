package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityItem;
import org.bukkit.entity.ItemDrop;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.CraftServer;

/**
 * Represents an item drop.
 * 
 * @author sk89q
 */
public class CraftItemDrop extends CraftEntity implements ItemDrop {
    private EntityItem item;
    
    public CraftItemDrop(CraftServer server, EntityItem entity) {
        super(server, entity);
        this.item = entity;
    }

    public ItemStack getItemStack() {
        return new CraftItemStack(item.a);
    }

    public void setItemStack(ItemStack stack) {
        item.a = new net.minecraft.server.ItemStack(stack.getTypeId(), stack.getAmount(), stack.getDurability());
    }

    @Override
    public String toString() {
        return "CraftItemDrop{" + "item=" + item + '}';
    }
}

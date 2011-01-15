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
    
    public CraftItemDrop(CraftServer server, EntityItem ent) {
        super(server, ent);
        this.item = ent;
    }

    public ItemStack getItemStack() {
        return new CraftItemStack(item.a);
    }
}

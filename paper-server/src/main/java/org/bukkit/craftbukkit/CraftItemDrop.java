package org.bukkit.craftbukkit;

import net.minecraft.server.EntityItem;
import org.bukkit.ItemDrop;
import org.bukkit.ItemStack;

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

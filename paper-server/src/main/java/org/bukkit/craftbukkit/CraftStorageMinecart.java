package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.EntityMinecart;

import org.bukkit.ItemStack;
import org.bukkit.StorageMinecart;

/**
 * A storage minecart.
 * 
 * @author sk89q
 */
public class CraftStorageMinecart extends CraftMinecart implements StorageMinecart {
    public CraftStorageMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
    }

    public int getSize() {
        return minecart.c();
    }

    public String getName() {
        return minecart.b();
    }

    public ItemStack getItem(int index) {
        return new CraftItemStack(minecart.a(index));
    }

    public List<ItemStack> getContents() {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (net.minecraft.server.ItemStack item: minecart.getContents()) {
            ItemStack i = null;
            if (item != null) {
                i = new CraftItemStack( item );
            }
            items.add(i);
        }

        return items;
    }
}

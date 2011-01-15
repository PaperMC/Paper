package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecart;

import org.bukkit.Inventory;
import org.bukkit.craftbukkit.CraftInventory;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.StorageMinecart;

/**
 * A storage minecart.
 * 
 * @author sk89q
 */
public class CraftStorageMinecart extends CraftMinecart implements StorageMinecart {
    private CraftInventory inventory;
    
    public CraftStorageMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
        inventory = new CraftInventory( entity );
    }

    public Inventory getInventory() {
        return inventory;
    }
}

package org.bukkit.craftbukkit;

import net.minecraft.server.EntityMinecart;
import org.bukkit.StorageMinecart;

/**
 * A storage minecart.
 * 
 * @author sk89q
 */
public class CraftStorageMinecart extends CraftMinecart
        implements StorageMinecart {
    public CraftStorageMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
    }

}

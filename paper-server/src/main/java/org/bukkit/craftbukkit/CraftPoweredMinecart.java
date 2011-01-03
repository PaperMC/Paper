package org.bukkit.craftbukkit;

import net.minecraft.server.EntityMinecart;
import org.bukkit.PoweredMinecart;

/**
 * A powered minecart.
 * 
 * @author sk89q
 */
public class CraftPoweredMinecart extends CraftMinecart
        implements PoweredMinecart {
    public CraftPoweredMinecart(CraftServer server, EntityMinecart entity) {
        super(server, entity);
    }

}

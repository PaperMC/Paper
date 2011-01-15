package org.bukkit.craftbukkit;

import net.minecraft.server.EntitySnowball;
import org.bukkit.entity.Snowball;

/**
 * A snowball.
 * 
 * @author sk89q
 */
public class CraftSnowball extends CraftEntity implements Snowball {
    public CraftSnowball(CraftServer server, EntitySnowball ent) {
        super(server, ent);
    }
}

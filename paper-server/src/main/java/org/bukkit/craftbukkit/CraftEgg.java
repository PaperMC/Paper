package org.bukkit.craftbukkit;

import net.minecraft.server.EntityEgg;
import org.bukkit.Egg;

/**
 * An egg.
 * 
 * @author sk89q
 */
public class CraftEgg extends CraftEntity implements Egg {
    public CraftEgg(CraftServer server, EntityEgg ent) {
        super(server, ent);
    }
}

package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEgg;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Egg;

/**
 * An egg.
 * 
 * @author sk89q
 */
public class CraftEgg extends CraftEntity implements Egg {
    public CraftEgg(CraftServer server, EntityEgg entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }
}

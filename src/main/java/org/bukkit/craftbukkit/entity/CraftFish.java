package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFish;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;

/**
 * An egg.
 * 
 * @author Cogito
 */
public class CraftFish extends CraftEntity implements Fish {
    public CraftFish(CraftServer server, EntityFish ent) {
        super(server, ent);
    }
}

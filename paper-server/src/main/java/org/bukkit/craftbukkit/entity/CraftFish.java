package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFish;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;

/**
 * A Fish.
 * 
 * @author Cogito
 */
public class CraftFish extends CraftEntity implements Fish {
    public CraftFish(CraftServer server, EntityFish entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftFish";
    }
}

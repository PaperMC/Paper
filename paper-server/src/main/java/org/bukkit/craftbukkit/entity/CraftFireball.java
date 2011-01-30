package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireball;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;

/**
 * A Fireball.
 * 
 * @author Cogito
 */
public class CraftFireball extends CraftEntity implements Fireball {
    public CraftFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }
}

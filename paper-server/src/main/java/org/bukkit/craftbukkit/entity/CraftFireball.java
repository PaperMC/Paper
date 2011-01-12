package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireball;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;

/**
 * An egg.
 * 
 * @author Cogito
 */
public class CraftFireball extends CraftEntity implements Fireball {
    public CraftFireball(CraftServer server, EntityFireball ent) {
        super(server, ent);
    }
}

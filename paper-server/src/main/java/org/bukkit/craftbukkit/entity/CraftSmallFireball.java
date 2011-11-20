package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SmallFireball;

public class CraftSmallFireball extends CraftFireball implements SmallFireball {
    public CraftSmallFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }
    
}

package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPigZombie;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, EntityPigZombie entity) {
        super(server, entity);
    }

}

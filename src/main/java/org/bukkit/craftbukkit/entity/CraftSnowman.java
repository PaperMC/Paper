package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCreature;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftCreature implements Snowman {
    public CraftSnowman(CraftServer server, EntityCreature entity) {
        super(server, entity);
    }
}

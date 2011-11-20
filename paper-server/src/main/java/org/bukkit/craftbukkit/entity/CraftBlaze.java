package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMonster;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Blaze;

public class CraftBlaze extends CraftMonster implements Blaze {
    public CraftBlaze(CraftServer server, EntityMonster entity) {
        super(server, entity);
    }
}

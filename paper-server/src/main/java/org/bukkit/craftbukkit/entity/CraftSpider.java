package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntitySpider;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Spider;

public class CraftSpider extends CraftMonster implements Spider {

    public CraftSpider(CraftServer server, EntitySpider entity) {
        super(server, entity);
    }

}

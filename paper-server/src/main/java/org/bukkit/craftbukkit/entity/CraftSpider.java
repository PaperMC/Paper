package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Spider;

public class CraftSpider extends CraftMonster implements Spider {

    public CraftSpider(CraftServer server, net.minecraft.world.entity.monster.Spider entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Spider getHandle() {
        return (net.minecraft.world.entity.monster.Spider) this.entity;
    }

    @Override
    public String toString() {
        return "CraftSpider";
    }
}

package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.CaveSpider;

public class CraftCaveSpider extends CraftSpider implements CaveSpider {
    public CraftCaveSpider(CraftServer server, net.minecraft.world.entity.monster.spider.CaveSpider entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.spider.CaveSpider getHandle() {
        return (net.minecraft.world.entity.monster.spider.CaveSpider) this.entity;
    }
}

package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftAbstractCubeMob implements MagmaCube, CraftEnemy {

    public CraftMagmaCube(CraftServer server, net.minecraft.world.entity.monster.cubemob.MagmaCube entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.cubemob.MagmaCube getHandle() {
        return (net.minecraft.world.entity.monster.cubemob.MagmaCube) this.entity;
    }
}

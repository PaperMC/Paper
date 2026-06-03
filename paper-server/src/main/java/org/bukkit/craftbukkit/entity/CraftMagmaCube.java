package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, net.minecraft.world.entity.monster.cubemob.MagmaCube entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.cubemob.MagmaCube getHandle() { // todo - snapshot
        return (net.minecraft.world.entity.monster.cubemob.MagmaCube) this.entity;
    }
}

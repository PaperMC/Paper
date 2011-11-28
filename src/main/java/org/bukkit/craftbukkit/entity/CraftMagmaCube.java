package org.bukkit.craftbukkit.entity;


import net.minecraft.server.EntityLavaSlime;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, EntityLavaSlime entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftMagmaCube";
    }

    public EntityLavaSlime getHandle() {
        return (EntityLavaSlime) super.getHandle();
    }
}

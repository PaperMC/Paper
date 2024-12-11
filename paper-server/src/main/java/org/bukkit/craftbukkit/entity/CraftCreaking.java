package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.creaking.Creaking;
import org.bukkit.craftbukkit.CraftServer;

public class CraftCreaking extends CraftMonster implements org.bukkit.entity.Creaking {

    public CraftCreaking(CraftServer server, Creaking entity) {
        super(server, entity);
    }

    @Override
    public Creaking getHandle() {
        return (Creaking) this.entity;
    }

    @Override
    public String toString() {
        return "CraftCreaking";
    }
}

package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ElderGuardian;

public class CraftElderGuardian extends CraftGuardian implements ElderGuardian {

    public CraftElderGuardian(CraftServer server, net.minecraft.world.entity.monster.ElderGuardian entity) {
        super(server, entity);
    }

    @Override
    public boolean isElder() {
        return true;
    }
}

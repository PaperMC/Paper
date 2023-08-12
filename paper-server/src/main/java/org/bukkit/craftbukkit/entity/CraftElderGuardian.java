package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntityGuardianElder;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ElderGuardian;

public class CraftElderGuardian extends CraftGuardian implements ElderGuardian {

    public CraftElderGuardian(CraftServer server, EntityGuardianElder entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftElderGuardian";
    }

    @Override
    public boolean isElder() {
        return true;
    }
}

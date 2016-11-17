package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityEvokerFangs;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;

public class CraftEvokerFangs extends CraftEntity implements EvokerFangs {

    public CraftEvokerFangs(CraftServer server, EntityEvokerFangs entity) {
        super(server, entity);
    }

    @Override
    public EntityEvokerFangs getHandle() {
        return (EntityEvokerFangs) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvokerFangs";
    }

    @Override
    public EntityType getType() {
        return EntityType.EVOKER_FANGS;
    }
}

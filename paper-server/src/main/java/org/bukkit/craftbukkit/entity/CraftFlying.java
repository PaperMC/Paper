package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFlying;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Flying;

public class CraftFlying extends CraftLivingEntity implements Flying {

    public CraftFlying(CraftServer server, EntityFlying entity) {
        super(server, entity);
    }

    @Override
    public EntityFlying getHandle() {
        return (EntityFlying) entity;
    }

    @Override
    public String toString() {
        return "CraftFlying";
    }
}

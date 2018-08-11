package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftMob implements ComplexLivingEntity { // Paper
    public CraftComplexLivingEntity(CraftServer server, EntityInsentient entity) { // Paper
        super(server, entity);
    }

    @Override
    public EntityInsentient getHandle() { // Paper
        return (EntityInsentient) entity; // Paper
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}

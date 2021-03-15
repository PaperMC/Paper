package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.EntitySalmon;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Salmon;

public class CraftSalmon extends CraftFish implements Salmon {

    public CraftSalmon(CraftServer server, EntitySalmon entity) {
        super(server, entity);
    }

    @Override
    public EntitySalmon getHandle() {
        return (EntitySalmon) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftSalmon";
    }

    @Override
    public EntityType getType() {
        return EntityType.SALMON;
    }
}

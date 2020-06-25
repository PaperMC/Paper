package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityStrider;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Strider;

public class CraftStrider extends CraftAnimals implements Strider {

    public CraftStrider(CraftServer server, EntityStrider entity) {
        super(server, entity);
    }

    @Override
    public EntityStrider getHandle() {
        return (EntityStrider) entity;
    }

    @Override
    public String toString() {
        return "CraftStrider";
    }

    @Override
    public EntityType getType() {
        return EntityType.STRIDER;
    }
}

package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityHoglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hoglin;

public class CraftHoglin extends CraftAnimals implements Hoglin {

    public CraftHoglin(CraftServer server, EntityHoglin entity) {
        super(server, entity);
    }

    @Override
    public EntityHoglin getHandle() {
        return (EntityHoglin) entity;
    }

    @Override
    public String toString() {
        return "CraftHoglin";
    }

    @Override
    public EntityType getType() {
        return EntityType.HOGLIN;
    }
}

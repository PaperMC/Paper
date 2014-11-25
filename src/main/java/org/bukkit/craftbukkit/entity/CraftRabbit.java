package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityRabbit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

public class CraftRabbit extends CraftAnimals implements Rabbit {

    public CraftRabbit(CraftServer server, EntityRabbit entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftRabbit";
    }

    @Override
    public EntityType getType() {
        return EntityType.RABBIT;
    }
}

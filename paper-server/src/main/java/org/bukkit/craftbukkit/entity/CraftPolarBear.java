package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.EntityPolarBear;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PolarBear;

public class CraftPolarBear extends CraftAnimals implements PolarBear {

    public CraftPolarBear(CraftServer server, EntityPolarBear entity) {
        super(server, entity);
    }
    @Override
    public EntityPolarBear getHandle() {
        return (EntityPolarBear) entity;
    }

    @Override
    public String toString() {
        return "CraftPolarBear";
    }
}

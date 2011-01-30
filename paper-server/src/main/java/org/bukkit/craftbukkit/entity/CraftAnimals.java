package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityAnimal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftCreature implements Animals{

    public CraftAnimals(CraftServer server, EntityAnimal entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftAnimals";
    }

}

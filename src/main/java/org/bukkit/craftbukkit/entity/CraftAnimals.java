package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityAnimals;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Animals;

public class CraftAnimals extends CraftCreature implements Animals{

    public CraftAnimals(CraftServer server, EntityAnimals entity) {
        super(server, entity);
    }



}

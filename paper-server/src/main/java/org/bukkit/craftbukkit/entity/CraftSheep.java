package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntitySheep;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {

    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, entity);
    }

}

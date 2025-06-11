package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.HappyGhast;

public class CraftHappyGhast extends CraftAnimals implements HappyGhast {
    public CraftHappyGhast(final CraftServer server, final net.minecraft.world.entity.animal.HappyGhast entity) {
        super(server, entity);
    }
}

package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.Animal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.HappyGhast;

public class CraftHappyGhast extends CraftAnimals implements HappyGhast {
    public CraftHappyGhast(final CraftServer server, final Animal entity) {
        super(server, entity);
    }
}

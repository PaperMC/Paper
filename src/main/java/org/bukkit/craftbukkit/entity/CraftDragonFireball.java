package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityDragonFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EntityType;

public class CraftDragonFireball extends CraftFireball implements DragonFireball {
    public CraftDragonFireball(CraftServer server, EntityDragonFireball entity) {
        super(server, entity);
    }

    @Override
    public EntityType getType() {
        return EntityType.DRAGON_FIREBALL;
    }
}

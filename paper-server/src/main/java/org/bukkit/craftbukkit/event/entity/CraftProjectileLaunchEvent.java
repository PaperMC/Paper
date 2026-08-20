package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class CraftProjectileLaunchEvent extends CraftEntitySpawnEvent implements ProjectileLaunchEvent {

    public CraftProjectileLaunchEvent(final Entity entity) {
        super(entity);
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) this.entity;
    }
}

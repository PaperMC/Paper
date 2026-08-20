package org.bukkit.event.entity;

import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;

/**
 * Called when a projectile is launched.
 */
public interface ProjectileLaunchEvent extends EntitySpawnEvent, Cancellable {

    @Override
    Projectile getEntity();
}

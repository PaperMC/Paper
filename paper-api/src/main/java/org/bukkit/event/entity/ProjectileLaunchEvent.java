package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;

/**
 * Called when a projectile is launched.
 */
public class ProjectileLaunchEvent extends EntitySpawnEvent implements Cancellable {
    private boolean cancelled;

    public ProjectileLaunchEvent(Entity what) {
        super(what);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) entity;
    }
}

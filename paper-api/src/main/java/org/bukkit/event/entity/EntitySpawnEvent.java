package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is spawned into a world.
 * <p>
 * If an Entity Spawn event is cancelled, the entity will not spawn.
 */
public class EntitySpawnEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;

    public EntitySpawnEvent(@NotNull final Entity spawnee) {
        super(spawnee);
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the location at which the entity is spawning.
     *
     * @return The location at which the entity is spawning
     */
    @NotNull
    public Location getLocation() {
        return getEntity().getLocation();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

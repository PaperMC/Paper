package org.bukkit.event.entity;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Thrown when a LivingEntity is tamed
 */
@SuppressWarnings("serial")
public class EntityTameEvent extends EntityEvent implements Cancellable {
    private boolean cancelled;
    private AnimalTamer owner;

    public EntityTameEvent(Entity entity, AnimalTamer owner) {
        super(Type.ENTITY_TAME, entity);
        this.owner = owner;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the owning AnimalTamer
     *
     * @return the owning AnimalTamer
     */
    public AnimalTamer getOwner() {
        return owner;
    }
}

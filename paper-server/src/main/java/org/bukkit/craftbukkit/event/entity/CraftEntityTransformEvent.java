package org.bukkit.craftbukkit.event.entity;

import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTransformEvent;

public class CraftEntityTransformEvent extends CraftEntityEvent implements EntityTransformEvent {

    private final List<Entity> transformedEntities;
    private final Entity transformedEntity;
    private final TransformReason transformReason;

    private boolean cancelled;

    public CraftEntityTransformEvent(final Entity original, final List<Entity> transformedEntities, final TransformReason transformReason) {
        super(original);
        this.transformedEntities = Collections.unmodifiableList(transformedEntities);
        this.transformedEntity = transformedEntities.getFirst();
        this.transformReason = transformReason;
    }

    @Override
    public List<Entity> getTransformedEntities() {
        return this.transformedEntities;
    }

    @Override
    public Entity getTransformedEntity() {
        return this.transformedEntity;
    }

    @Override
    public TransformReason getTransformReason() {
        return this.transformReason;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityTransformEvent.getHandlerList();
    }
}

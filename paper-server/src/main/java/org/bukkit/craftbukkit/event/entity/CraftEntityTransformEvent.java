package org.bukkit.craftbukkit.event.entity;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTransformEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftEntityTransformEvent extends CraftEntityEvent implements EntityTransformEvent {

    private final List<Entity> transformedEntities;
    private final Entity transformedEntity;
    private final TransformReason reason;

    private boolean cancelled;

    public CraftEntityTransformEvent(final Entity original, final List<Entity> transformedEntities, final TransformReason reason) {
        super(original);
        this.transformedEntities = Collections.unmodifiableList(transformedEntities);
        this.transformedEntity = transformedEntities.getFirst();
        this.reason = reason;
    }

    public CraftEntityTransformEvent(
        final net.minecraft.world.entity.Entity original,
        final List<? extends net.minecraft.world.entity.Entity> transformedEntities,
        final TransformReason reason
    ) {
        this(
            original.getBukkitEntity(),
            Lists.transform(transformedEntities, net.minecraft.world.entity.Entity::getBukkitEntity),
            reason
        );
    }

    public CraftEntityTransformEvent(
        final net.minecraft.world.entity.Entity original,
        final net.minecraft.world.entity.Entity transformedEntity,
        final TransformReason reason
    ) {
        super(original.getBukkitEntity());
        this.transformedEntity = transformedEntity.getBukkitEntity();
        this.transformedEntities = List.of(this.transformedEntity);
        this.reason = reason;
    }

    @Override
    public @Unmodifiable List<Entity> getTransformedEntities() {
        return this.transformedEntities;
    }

    @Override
    public Entity getTransformedEntity() {
        return this.transformedEntity;
    }

    @Override
    public TransformReason getTransformReason() {
        return this.reason;
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

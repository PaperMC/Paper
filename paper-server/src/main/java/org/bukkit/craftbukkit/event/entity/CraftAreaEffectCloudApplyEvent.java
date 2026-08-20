package org.bukkit.craftbukkit.event.entity;

import java.util.List;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

public class CraftAreaEffectCloudApplyEvent extends CraftEntityEvent implements AreaEffectCloudApplyEvent {

    private final List<LivingEntity> affectedEntities;
    private boolean cancelled;

    public CraftAreaEffectCloudApplyEvent(final AreaEffectCloud entity, final List<LivingEntity> affectedEntities) {
        super(entity);
        this.affectedEntities = affectedEntities;
    }

    @Override
    public AreaEffectCloud getEntity() {
        return (AreaEffectCloud) this.entity;
    }

    @Override
    public List<LivingEntity> getAffectedEntities() {
        return this.affectedEntities;
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
        return AreaEffectCloudApplyEvent.getHandlerList();
    }
}

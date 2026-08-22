package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class PaperEntityJumpEvent extends CraftEntityEvent implements EntityJumpEvent {

    private boolean cancelled;

    public PaperEntityJumpEvent(final LivingEntity entity) {
        super(entity);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
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
        return EntityJumpEvent.getHandlerList();
    }
}

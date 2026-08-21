package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class PaperEntityLungeEvent extends CraftEntityEvent implements EntityLungeEvent {

    private int lungePower;
    private boolean cancelled;

    public PaperEntityLungeEvent(final LivingEntity entity, final int lungePower) {
        super(entity);
        this.lungePower = lungePower;
    }

    @Override
    public int getLungePower() {
        return this.lungePower;
    }

    @Override
    public void setLungePower(final int lungePower) {
        this.lungePower = lungePower;
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
        return EntityLungeEvent.getHandlerList();
    }
}

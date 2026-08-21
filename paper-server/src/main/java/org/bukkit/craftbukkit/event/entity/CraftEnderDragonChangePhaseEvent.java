package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.jspecify.annotations.Nullable;

public class CraftEnderDragonChangePhaseEvent extends CraftEntityEvent implements EnderDragonChangePhaseEvent {

    private final EnderDragon.Phase currentPhase;
    private EnderDragon.Phase newPhase;

    private boolean cancelled;

    public CraftEnderDragonChangePhaseEvent(final EnderDragon dragon, final EnderDragon.@Nullable Phase currentPhase, final EnderDragon.Phase newPhase) {
        super(dragon);
        this.currentPhase = currentPhase;
        this.newPhase = newPhase;
    }

    @Override
    public EnderDragon getEntity() {
        return (EnderDragon) this.entity;
    }

    @Override
    public EnderDragon.@Nullable Phase getCurrentPhase() {
        return this.currentPhase;
    }

    @Override
    public EnderDragon.Phase getNewPhase() {
        return this.newPhase;
    }

    @Override
    public void setNewPhase(final EnderDragon.Phase newPhase) {
        Preconditions.checkArgument(newPhase != null, "New dragon phase cannot be null");
        this.newPhase = newPhase;
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
        return EnderDragonChangePhaseEvent.getHandlerList();
    }
}

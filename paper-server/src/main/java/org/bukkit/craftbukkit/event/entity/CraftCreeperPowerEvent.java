package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.jspecify.annotations.Nullable;

public class CraftCreeperPowerEvent extends CraftEntityEvent implements CreeperPowerEvent {

    private final PowerCause cause;
    private LightningStrike bolt;

    private boolean cancelled;

    public CraftCreeperPowerEvent(final Creeper creeper, final LightningStrike bolt, final PowerCause cause) {
        this(creeper, cause);
        this.bolt = bolt;
    }

    public CraftCreeperPowerEvent(final Creeper creeper, final PowerCause cause) {
        super(creeper);
        this.cause = cause;
    }

    @Override
    public Creeper getEntity() {
        return (Creeper) this.entity;
    }

    @Override
    public @Nullable LightningStrike getLightning() {
        return this.bolt;
    }

    @Override
    public PowerCause getCause() {
        return this.cause;
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
        return CreeperPowerEvent.getHandlerList();
    }
}

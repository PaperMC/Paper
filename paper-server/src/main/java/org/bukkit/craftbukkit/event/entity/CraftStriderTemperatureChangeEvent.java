package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Strider;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.StriderTemperatureChangeEvent;

public class CraftStriderTemperatureChangeEvent extends CraftEntityEvent implements StriderTemperatureChangeEvent {

    private final boolean shivering;
    private boolean cancelled;

    public CraftStriderTemperatureChangeEvent(final Strider strider, final boolean shivering) {
        super(strider);
        this.shivering = shivering;
    }

    @Override
    public Strider getEntity() {
        return (Strider) this.entity;
    }

    @Override
    public boolean isShivering() {
        return this.shivering;
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
        return StriderTemperatureChangeEvent.getHandlerList();
    }
}

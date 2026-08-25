package org.bukkit.craftbukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.weather.ThunderChangeEvent;

public class CraftThunderChangeEvent extends CraftWeatherEvent implements ThunderChangeEvent {

    private final boolean newThunderState;
    private final Cause cause;

    private boolean cancelled;

    public CraftThunderChangeEvent(final World world, final boolean newThunderState, final Cause cause) {
        super(world);
        this.newThunderState = newThunderState;
        this.cause = cause;
    }

    @Override
    public boolean toThunderState() {
        return this.newThunderState;
    }

    @Override
    public Cause getCause() {
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
        return ThunderChangeEvent.getHandlerList();
    }
}

package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.TurtleStartDiggingEvent;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Turtle;
import org.bukkit.event.HandlerList;

public class PaperTurtleStartDiggingEvent extends CraftEntityEvent implements TurtleStartDiggingEvent {

    private final Location location;
    private boolean cancelled;

    public PaperTurtleStartDiggingEvent(final Turtle turtle, final Location location) {
        super(turtle);
        this.location = location;
    }

    @Override
    public Turtle getEntity() {
        return (Turtle) this.entity;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
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
        return TurtleStartDiggingEvent.getHandlerList();
    }
}

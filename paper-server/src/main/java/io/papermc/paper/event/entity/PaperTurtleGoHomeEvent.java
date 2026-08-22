package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.TurtleGoHomeEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Turtle;
import org.bukkit.event.HandlerList;

public class PaperTurtleGoHomeEvent extends CraftEntityEvent implements TurtleGoHomeEvent {

    private boolean cancelled;

    public PaperTurtleGoHomeEvent(final Turtle turtle) {
        super(turtle);
    }

    @Override
    public Turtle getEntity() {
        return (Turtle) this.entity;
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
        return TurtleGoHomeEvent.getHandlerList();
    }
}

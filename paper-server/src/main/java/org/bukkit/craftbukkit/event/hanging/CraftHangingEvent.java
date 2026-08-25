package org.bukkit.craftbukkit.event.hanging;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingEvent;

public abstract class CraftHangingEvent extends CraftEvent implements HangingEvent {

    protected Hanging hanging;

    protected CraftHangingEvent(final Hanging hanging) {
        this.hanging = hanging;
    }

    @Override
    public Hanging getEntity() {
        return this.hanging;
    }
}

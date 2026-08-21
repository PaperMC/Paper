package org.bukkit.craftbukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.util.Vector;

public class CraftEntityPortalExitEvent extends CraftEntityTeleportEvent implements EntityPortalExitEvent {

    private final Vector before;
    private Vector after;

    public CraftEntityPortalExitEvent(final Entity entity, final Location from, final Location to, final Vector before, final Vector after) {
        super(entity, from, to);
        this.before = before;
        this.after = after;
    }

    @Override
    public Vector getBefore() {
        return this.before.clone();
    }

    @Override
    public Vector getAfter() {
        return this.after.clone();
    }

    @Override
    public void setAfter(final Vector after) {
        this.after = after.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return EntityPortalExitEvent.getHandlerList();
    }
}

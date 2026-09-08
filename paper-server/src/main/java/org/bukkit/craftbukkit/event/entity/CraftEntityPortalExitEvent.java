package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftVector;
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

    public CraftEntityPortalExitEvent(final net.minecraft.world.entity.Entity entity, final Location to, final Vec3 after) {
        final Entity e = entity.getBukkitEntity();
        this(e, e.getLocation(), to, e.getVelocity(), CraftVector.toBukkit(after));
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

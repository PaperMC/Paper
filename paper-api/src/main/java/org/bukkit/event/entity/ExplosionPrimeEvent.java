package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

public class ExplosionPrimeEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private float radius;
    private boolean fire;

    public ExplosionPrimeEvent(Entity what, float radius, boolean fire) {
        super(Type.EXPLOSION_PRIME, what);
        this.cancel = false;
        this.radius = radius;
        this.fire = fire;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean getFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

}

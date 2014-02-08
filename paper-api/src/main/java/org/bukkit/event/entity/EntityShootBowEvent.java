package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a LivingEntity shoots a bow firing an arrow
 */
public class EntityShootBowEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack bow;
    private Entity projectile;
    private final float force;
    private boolean cancelled;

    public EntityShootBowEvent(final LivingEntity shooter, final ItemStack bow, final Projectile projectile, final float force) {
        super(shooter);
        this.bow = bow;
        this.projectile = projectile;
        this.force = force;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }

    /**
     * Gets the bow ItemStack used to fire the arrow.
     *
     * @return the bow involved in this event
     */
    public ItemStack getBow() {
        return bow;
    }

    /**
     * Gets the projectile which will be launched by this event
     *
     * @return the launched projectile
     */
    public Entity getProjectile() {
        return projectile;
    }

    /**
     * Replaces the projectile which will be launched
     *
     * @param projectile the new projectile
     */
    public void setProjectile(Entity projectile) {
        this.projectile = projectile;
    }

    /**
     * Gets the force the arrow was launched with
     *
     * @return bow shooting force, up to 1.0
     */
    public float getForce() {
        return force;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

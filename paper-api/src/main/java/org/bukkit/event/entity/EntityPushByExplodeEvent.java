package org.bukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an entity explodes interacting with not living entities.
 */
@NullMarked
public class EntityPushByExplodeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity pusher;
    private final Entity pushee;
    private Vector knockback;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityPushByExplodeEvent(final Entity pusher, final Entity pushee, final Vector knockback) {
        super(pusher);
        this.pusher = pusher;
        this.pushee = pushee;
        this.knockback = knockback;
    }

    /**
     * The Entity that is responsible for pushing the other Entity.
     *
     * @return the Entity
     */
    public Entity getPusher() {
        return pusher;
    }

    /**
     * The Entity that is pushed by the explosion.
     *
     * @return the pushed Entity
     */
    public Entity getPushee() {
        return pushee;
    }

    /**
     * Gets the knockback force that will be applied to the entity. <br>
     * This value is read-only, changes made to it <b>will not</b> have any
     * effect on the final knockback received. Use {@link #setKnockback(Vector)}
     * to make changes.
     *
     * @return the knockback
     */
    public Vector getKnockback() {
        return this.knockback.clone();
    }

    /**
     * Sets the knockback force that will be applied to the entity.
     *
     * @param knockback the knockback
     */
    public void setKnockback(final Vector knockback) {
        this.knockback = knockback.clone();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

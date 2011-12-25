package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when a creature targets or untargets another entity
 */
@SuppressWarnings("serial")
public class EntityTargetEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private Entity target;
    private TargetReason reason;

    public EntityTargetEvent(Entity entity, Entity target, TargetReason reason) {
        super(Type.ENTITY_TARGET, entity);
        this.target = target;
        this.cancel = false;
        this.reason = reason;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the reason for the targeting
     *
     * @return The reason
     */
    public TargetReason getReason() {
        return reason;
    }

    /**
     * Get the entity that this is targeting.
     * This will be null in the case that the event is called when
     * the mob forgets its target.
     *
     * @return The entity
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Set the entity that you want the mob to target instead.
     * It is possible to be null, null will cause the entity to be
     * target-less.
     *
     * This is different from cancelling the event. Cancelling the event
     * will cause the entity to keep an original target, while setting to be
     * null will cause the entity to be reset
     *
     * @param target The entity to target
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * An enum to specify the reason for the targeting
     */
    public enum TargetReason {

        /**
         * When the entity's target has died, and so it no longer targets it
         */
        TARGET_DIED,
        /**
         * When the entity doesn't have a target, so it attacks the nearest
         * player
         */
        CLOSEST_PLAYER,
        /**
         * When the target attacks the entity, so entity targets it
         */
        TARGET_ATTACKED_ENTITY,
        /**
         * When the target attacks a fellow pig zombie, so the whole group
         * will target him with this reason.
         */
        PIG_ZOMBIE_TARGET,
        /**
         * When the target is forgotten for whatever reason.
         * Currently only occurs in with spiders when there is a high brightness
         */
        FORGOT_TARGET,
        /**
         * When the target attacks the owner of the entity, so the entity targets it.
         */
        TARGET_ATTACKED_OWNER,
        /**
         * When the owner of the entity attacks the target attacks, so the entity targets it.
         */
        OWNER_ATTACKED_TARGET,
        /**
         * When the entity has no target, so the entity randomly chooses one.
         */
        RANDOM_TARGET,
        /**
         * For custom calls to the event
         */
        CUSTOM
    }
}

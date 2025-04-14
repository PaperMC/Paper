package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a creature targets or untargets another entity
 */
public class EntityTargetEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Entity target;
    private final TargetReason reason;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityTargetEvent(@NotNull final Entity entity, @Nullable final Entity target, @NotNull final TargetReason reason) {
        super(entity);
        this.target = target;
        this.reason = reason;
    }

    /**
     * Returns the reason for the targeting
     *
     * @return The reason
     */
    @NotNull
    public TargetReason getReason() {
        return this.reason;
    }

    /**
     * Get the entity that this is targeting.
     * <p>
     * This will be {@code null} in the case that the event is called when the mob
     * forgets its target.
     *
     * @return The entity
     */
    @Nullable
    public Entity getTarget() {
        return this.target;
    }

    /**
     * Set the entity that you want the mob to target instead.
     * <p>
     * It is possible to be {@code null}, {@code null} will cause the entity to be
     * target-less.
     * <p>
     * This is different from cancelling the event. Cancelling the event will
     * cause the entity to keep an original target, while setting to be null
     * will cause the entity to be reset.
     *
     * @param target The entity to target
     */
    public void setTarget(@Nullable Entity target) {
        this.target = target;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
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
         *
         * @deprecated obsoleted by {@link #TARGET_ATTACKED_NEARBY_ENTITY}
         */
        @Deprecated(since = "1.13", forRemoval = true)
        PIG_ZOMBIE_TARGET,
        /**
         * When the target is forgotten for whatever reason.
         */
        FORGOT_TARGET,
        /**
         * When the target attacks the owner of the entity, so the entity
         * targets it.
         */
        TARGET_ATTACKED_OWNER,
        /**
         * When the owner of the entity attacks the target attacks, so the
         * entity targets it.
         */
        OWNER_ATTACKED_TARGET,
        /**
         * When the entity has no target, so the entity randomly chooses one.
         */
        RANDOM_TARGET,
        /**
         * When an entity selects a target while defending a village.
         */
        DEFEND_VILLAGE,
        /**
         * When the target attacks a nearby entity of the same type, so the entity targets it
         */
        TARGET_ATTACKED_NEARBY_ENTITY,
        /**
         * When a zombie targeting an entity summons reinforcements, so the reinforcements target the same entity
         */
        REINFORCEMENT_TARGET,
        /**
         * When an entity targets another entity after colliding with it.
         */
        COLLISION,
        /**
         * For custom calls to the event.
         */
        CUSTOM,
        /**
         * When the entity doesn't have a target, so it attacks the nearest
         * entity
         */
        CLOSEST_ENTITY,
        /**
         * When a raiding entity selects the same target as one of its compatriots.
         */
        FOLLOW_LEADER,
        /**
         * When another entity tempts this entity by having a desired item such
         * as wheat in its hand.
         */
        TEMPT,
        /**
         * When the target is in a different dimension
         */
        TARGET_OTHER_LEVEL,
        /**
         * When the target is in creative or spectator gamemode, or the difficulty is peaceful, or other reasons
         */
        TARGET_INVALID,
        /**
         * A currently unknown reason for the entity changing target.
         */
        UNKNOWN;
    }
}

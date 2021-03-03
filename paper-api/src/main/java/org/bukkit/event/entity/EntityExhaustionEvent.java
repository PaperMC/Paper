package org.bukkit.event.entity;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a human entity experiences exhaustion.
 *
 * An exhaustion level greater than 4.0 causes a decrease in saturation by 1.
 */
public class EntityExhaustionEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final ExhaustionReason exhaustionReason;
    private float exhaustion;
    private boolean cancel;

    public EntityExhaustionEvent(@NotNull HumanEntity who, @NotNull ExhaustionReason exhaustionReason, float exhaustion) {
        super(who);
        this.exhaustionReason = exhaustionReason;
        this.exhaustion = exhaustion;
    }

    /**
     * Gets the {@link ExhaustionReason} for this event
     *
     * @return the exhaustion reason
     */
    @NotNull
    public ExhaustionReason getExhaustionReason() {
        return exhaustionReason;
    }

    /**
     * Get the amount of exhaustion to add to the player's current exhaustion.
     *
     * @return amount of exhaustion
     */
    public float getExhaustion() {
        return exhaustion;
    }

    /**
     * Set the exhaustion to apply to the player.
     *
     * The maximum exhaustion that a player can have is 40. No error will be
     * thrown if this limit is hit. This value may be negative, but there is
     * unknown behavior for when exhaustion is below 0.
     *
     * @param exhaustion new exhaustion to add
     */
    public void setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
    }

    @NotNull
    @Override
    public HumanEntity getEntity() {
        return (HumanEntity) super.getEntity();
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * The reason for why a PlayerExhaustionEvent takes place
     */
    public enum ExhaustionReason {

        /**
         * Player mines a block
         */
        BLOCK_MINED,
        /**
         * Player has the hunger potion effect
         */
        HUNGER_EFFECT,
        /**
         * Player takes damage
         */
        DAMAGED,
        /**
         * Player attacks another entity
         */
        ATTACK,
        /**
         * Player is sprint jumping
         */
        JUMP_SPRINT,
        /**
         * Player jumps
         */
        JUMP,
        /**
         * Player swims one centimeter
         */
        SWIM,
        /**
         * Player walks underwater one centimeter
         */
        WALK_UNDERWATER,
        /**
         * Player moves on the surface of water one centimeter
         */
        WALK_ON_WATER,
        /**
         * Player sprints one centimeter
         */
        SPRINT,
        /**
         * Player crouches one centimeter (does not effect exhaustion, but fires
         * nonetheless)
         */
        CROUCH,
        /**
         * Player walks one centimeter (does not effect exhaustion, but fires
         * nonetheless)
         */
        WALK,
        /**
         * Player regenerated health
         */
        REGEN,
        /**
         * Unknown exhaustion reason
         */
        UNKNOWN
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

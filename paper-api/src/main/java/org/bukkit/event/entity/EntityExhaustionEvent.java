package org.bukkit.event.entity;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a human entity experiences exhaustion.
 * <p>
 * An exhaustion level greater than 4.0 causes a decrease in saturation by 1.
 */
public interface EntityExhaustionEvent extends EntityEvent, Cancellable {

    @Override
    HumanEntity getEntity();

    /**
     * Gets the {@link ExhaustionReason} for this event
     *
     * @return the exhaustion reason
     */
    ExhaustionReason getExhaustionReason();

    /**
     * Get the amount of exhaustion to add to the player's current exhaustion.
     *
     * @return amount of exhaustion
     */
    float getExhaustion();

    /**
     * Set the exhaustion to apply to the player.
     * <p>
     * The maximum exhaustion that a player can have is 40. No error will be
     * thrown if this limit is hit. This value may be negative, but there is
     * unknown behavior for when exhaustion is below 0.
     *
     * @param exhaustion new exhaustion to add
     */
    void setExhaustion(float exhaustion);

    /**
     * The reason for why a PlayerExhaustionEvent takes place
     */
    enum ExhaustionReason {

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
         * Player was affected by an enchantment
         * @see <a href="https://minecraft.wiki/w/Enchantment_definition#apply_exhaustion">Minecraft Wiki: Enchantment Effects "apply_exhaustion"</a>
         */
        ENCHANTMENT_EFFECT,
        /**
         * Unknown exhaustion reason
         */
        UNKNOWN
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

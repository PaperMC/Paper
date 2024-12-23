package org.bukkit.entity;

import org.bukkit.World;
import org.bukkit.boss.DragonBattle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an Ender Dragon
 *
 * @since 1.0.0
 */
public interface EnderDragon extends ComplexLivingEntity, Boss, Mob, Enemy {

    /**
     * Represents a phase or action that an Ender Dragon can perform.
     */
    enum Phase {
        /**
         * The dragon will circle outside the ring of pillars if ender
         * crystals remain or inside the ring if not.
         */
        CIRCLING,
        /**
         * The dragon will fly towards a targeted player and shoot a
         * fireball when within 64 blocks.
         */
        STRAFING,
        /**
         * The dragon will fly towards the empty portal (approaching
         * from the other side, if applicable).
         */
        FLY_TO_PORTAL,
        /**
         * The dragon will land on the portal. If the dragon is not near
         * the portal, it will fly to it before mounting.
         */
        LAND_ON_PORTAL,
        /**
         * The dragon will leave the portal.
         */
        LEAVE_PORTAL,
        /**
         * The dragon will attack with dragon breath at its current location.
         */
        BREATH_ATTACK,
        /**
         * The dragon will search for a player to attack with dragon breath.
         * If no player is close enough to the dragon for 5 seconds, the
         * dragon will charge at a player within 150 blocks or will take off
         * and begin circling if no player is found.
         */
        SEARCH_FOR_BREATH_ATTACK_TARGET,
        /**
         * The dragon will roar before performing a breath attack.
         */
        ROAR_BEFORE_ATTACK,
        /**
         * The dragon will charge a player.
         */
        CHARGE_PLAYER,
        /**
         * The dragon will fly to the vicinity of the portal and die.
         */
        DYING,
        /**
         * The dragon will hover at its current location, not performing any actions.
         */
        HOVER
    }

    /**
     * Gets the current phase that the dragon is performing.
     *
     * @return the current phase
     * @since 1.9.4
     */
    @NotNull
    Phase getPhase();

    /**
     * Sets the next phase for the dragon to perform.
     *
     * @param phase the next phase
     */
    void setPhase(@NotNull Phase phase);

    /**
     * Get the {@link DragonBattle} associated with this EnderDragon.
     * <br>
     * This will return null for the following reasons:
     * <ul>
     *     <li>The EnderDragon is not in the End dimension</li>
     *     <li>The EnderDragon was summoned by command/API</li>
     * </ul>
     *
     * @return the dragon battle
     *
     * @see World#getEnderDragonBattle()
     * @since 1.15.2
     */
    @Nullable
    DragonBattle getDragonBattle();

    /**
     * Get the current time in ticks relative to the start of this dragon's
     * death animation.
     *
     * If this dragon is alive, 0 will be returned. This value will never exceed
     * 200 (the length of the animation).
     *
     * @return this dragon's death animation ticks
     * @since 1.15.2
     */
    int getDeathAnimationTicks();

    // Paper start

    /**
     * Get the podium location used by the ender dragon.
     *
     * @return the podium location of the dragon
     * @since 1.18.2
     */
    @NotNull
    org.bukkit.Location getPodium();

    /**
     * Sets the location of the podium for the ender dragon.
     *
     * @param location the location of the podium or null to use the default podium location (exit portal of the end)
     * @since 1.18.2
     */
    void setPodium(@Nullable org.bukkit.Location location);
    // Paper end
}

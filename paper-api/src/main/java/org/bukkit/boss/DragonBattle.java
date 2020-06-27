package org.bukkit.boss;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a dragon battle state for a world with an end environment.
 */
public interface DragonBattle {

    /**
     * Get the {@link EnderDragon} active in this battle.
     *
     * Will return null if the dragon has been slain.
     *
     * @return the ender dragon. null if dead
     */
    @Nullable
    public EnderDragon getEnderDragon();

    /**
     * Get the boss bar to be displayed for this dragon battle.
     *
     * @return the boss bar
     */
    @NotNull
    public BossBar getBossBar();

    /**
     * Get the location of the end portal.
     *
     * This location will be at the center of the base (bottom) of the portal.
     *
     * @return the end portal location or null if not generated
     */
    @Nullable
    public Location getEndPortalLocation();

    /**
     * Generate the end portal.
     *
     * @param withPortals whether or not end portal blocks should be generated
     *
     * @return true if generated, false if already present
     */
    public boolean generateEndPortal(boolean withPortals);

    /**
     * Check whether or not the first dragon has been killed already.
     *
     * @return true if killed before, false otherwise
     */
    public boolean hasBeenPreviouslyKilled();

    /**
     * Initiate a respawn sequence to summon the dragon as though a player has
     * placed 4 end crystals on the portal.
     */
    public void initiateRespawn();

    /**
     * Get this battle's current respawn phase.
     *
     * @return the current respawn phase.
     */
    @NotNull
    public RespawnPhase getRespawnPhase();

    /**
     * Set the dragon's respawn phase.
     *
     * This method will is unsuccessful if a dragon respawn is not in progress.
     *
     * @param phase the phase to set
     *
     * @return true if successful, false otherwise
     *
     * @see #initiateRespawn()
     */
    public boolean setRespawnPhase(@NotNull RespawnPhase phase);

    /**
     * Reset the crystals located on the obsidian pillars (remove their beam
     * targets and invulnerability).
     */
    public void resetCrystals();

    /**
     * Represents a phase in the dragon respawn process.
     */
    public enum RespawnPhase {

        /**
         * The crystal beams are directed upwards into the sky.
         */
        START,
        /**
         * The crystal beams remain directed upwards.
         */
        PREPARING_TO_SUMMON_PILLARS,
        /**
         * The crystal beams are directed from pillar to pillar, regenerating
         * their crystals if necessary.
         */
        SUMMONING_PILLARS,
        /**
         * All crystals (including those from the pillars) are aimed towards the
         * sky. Shortly thereafter summoning the dragon and destroying the
         * crystals used to initiate the dragon's respawn.
         */
        SUMMONING_DRAGON,
        /**
         * The end of the respawn sequence. The dragon is actually summoned.
         */
        END,
        /**
         * No respawn is in progress.
         */
        NONE;
    }
}

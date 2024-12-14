package org.bukkit.entity;

import org.bukkit.Location;

/**
 * @since 1.13
 */
public interface Dolphin extends Ageable, WaterMob { // Paper start - Dolphin API

    /**
     * Gets the moistness level of this dolphin
     *
     * @since 1.18.1
     */
    int getMoistness();

    /**
     * Sets the moistness of this dolphin, once this is less than 0 the dolphin will start to take damage.
     *
     * @param moistness moistness level
     * @since 1.18.1
     */
    void setMoistness(int moistness);

    /**
     * Sets if this dolphin was fed a fish.
     *
     * @param hasFish has a fish
     * @since 1.18.1
     */
    void setHasFish(boolean hasFish);

    /**
     * Gets if this dolphin has a fish.
     *
     * @return has a fish
     * @since 1.18.1
     */
    boolean hasFish();

    /**
     * Gets the treasure location this dolphin tries to guide players to.
     * <p>
     * This value is calculated if the player has fed the dolphin a fish, and it tries to start the {@link com.destroystokyo.paper.entity.ai.VanillaGoal#DOLPHIN_SWIM_TO_TREASURE} goal.
     *
     *  @return calculated closest treasure location
     * @since 1.18.1
     */
    @org.jetbrains.annotations.NotNull
    Location getTreasureLocation();

    /**
     * Sets the treasure location that this dolphin will try to lead the player to.
     * This only has an effect if the dolphin is currently leading a player, as this value is recalculated next time it leads a player.
     * <p>
     * The world of the location does not matter, as the dolphin will always use the world it is currently in.
     *
     *  @param location location to guide to
     * @since 1.18.1
     */
    void setTreasureLocation(@org.jetbrains.annotations.NotNull Location location);
} // Paper end - Dolphin API

package io.papermc.paper.world.biome.effects;

import org.bukkit.Sound;
import org.jetbrains.annotations.ApiStatus;

/**
 * Settings for mood sound.
 */
@ApiStatus.Experimental
public interface MoodSound {

    /**
     * The mood sound.
     *
     * @return the mood sound.
     */
    Sound sound();

    /**
     * The minimum delay between mood sound plays.
     *
     * @return the minimum delay.
     */
    int tickDelay();

    /**
     * Determines the cubic range of possible positions
     * to find place to play the mood sound.
     * The player is at the center of the cubic range,
     * and the edge length is {@code 2 * blockSearchExtent}.
     *
     * @return the block search extent.
     */
    int blockSearchExtent();

    /**
     * The higher the value makes the
     * sound source further away from the player.
     *
     * @return the sound position offset.
     */
    double offset();
}

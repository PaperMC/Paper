package io.papermc.paper.world.biome.effects;

import org.bukkit.Sound;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface MusicEntry {

    /**
     * The music sound.
     *
     * @return the music sound.
     */
    Sound sound();

    /**
     * The min delay between two music entries.
     *
     * @return the min delay.
     */
    int minDelay();

    /**
     * The max delay between two music entries.
     *
     * @return the max delay.
     */
    int maxDelay();

    /**
     * Whether to replace the music which is already playing.
     *
     * @return true if the music should be replaced.
     */
    boolean replaceCurrentMusic();

    /**
     * The weight of the music.
     *
     * @return the weight.
     */
    int weight();
}

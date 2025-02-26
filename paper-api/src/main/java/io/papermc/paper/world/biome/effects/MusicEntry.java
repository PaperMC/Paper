package io.papermc.paper.world.biome.effects;

import org.bukkit.Sound;

public interface MusicEntry {
    /**
     * One sound event — The music sound.
     *
     * @return the music sound.
     */
    Sound sound();

    /**
     * The min delay between two music.
     *
     * @return the min delay.
     */
    int minDelay();

    /**
     * The max delay between two music.
     *
     * @return the max delay.
     */
    int maxDelay();

    /**
     * Whether to replace music which is already playing.
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

package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.JukeboxSong;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

/**
 * A data-centric version-specific registry entry for the {@link JukeboxSong} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface JukeboxSongRegistryEntry {

    /**
     * Provides the sound key associated with this jukebox song.
     *
     * @return the sound key.
     */
    Key sound();

    /**
     * Provides the description of the jukebox song, displayed in the tooltip of the item.
     *
     * @return the description component.
     */
    Component description();

    /**
     * Provides the length of the song in seconds. Determines the length of the redstone output of the jukebox.
     * <p>
     * <strong>Note</strong> If the sound is longer, it is cut off after this time.
     *
     * @return the length in seconds.
     */
    float lengthInSeconds();

    /**
     * Provides the redstone power output by a comparator when placed next to a jukebox playing this song.
     *
     * @return the comparator output, between 0 and 15.
     */
    @Range(from = 0, to = 15) int comparatorOutput();

    /**
     * A mutable builder for the {@link JukeboxSongRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #sound(Key)}</li>
     *     <li>{@link #description(Component)}</li>
     *     <li>{@link #lengthInSeconds(float)}</li>
     *     <li>{@link #comparatorOutput(int)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends JukeboxSongRegistryEntry, RegistryBuilder<JukeboxSong> {

        /**
         * Sets the sound key associated with this jukebox song.
         *
         * @param sound the sound key.
         * @return this builder instance.
         * @see JukeboxSongRegistryEntry#sound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(Key sound);

        /**
         * Sets the description of the jukebox song, displayed in the tooltip of the item.
         *
         * @param description the description component
         * @return this builder instance.
         * @see JukeboxSongRegistryEntry#description()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        /**
         * Sets the length of the song in seconds. Determines the length of the redstone output of the jukebox.
         * <p>
         * <strong>Note</strong> If the sound is longer, it is cut off after this time.
         * @param lengthInSeconds the length in seconds.
         * @return this builder instance.
         * @see JukeboxSongRegistryEntry#lengthInSeconds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder lengthInSeconds(float lengthInSeconds);

        /**
         * Sets the redstone power output by a comparator when placed next to a jukebox playing this song.
         *
         * @param comparatorOutput the comparator output, between 0 and 15.
         * @return this builder instance.
         * @see JukeboxSongRegistryEntry#comparatorOutput()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder comparatorOutput(@Range(from = 0, to = 15) int comparatorOutput);

    }
}

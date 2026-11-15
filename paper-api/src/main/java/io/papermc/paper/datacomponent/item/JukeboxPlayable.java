package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.TypedKey;
import org.bukkit.JukeboxSong;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Holds the jukebox song for an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#JUKEBOX_PLAYABLE
 */
@ApiStatus.NonExtendable
public interface JukeboxPlayable  {

    @Contract(value = "_ -> new", pure = true)
    static JukeboxPlayable.Builder jukeboxPlayable(final JukeboxSong song) {
        return ItemComponentTypesBridge.bridge().jukeboxPlayable(song);
    }

    /**
     * Creates a builder for a jukebox playable component.
     * <p>
     * Unlike {@link #jukeboxPlayable(JukeboxSong)}, this can be used while the jukebox song
     * registry is still being loaded, such as in registry events.
     *
     * @param song the key of the song
     * @return a new builder
     */
    @Contract(value = "_ -> new", pure = true)
    static JukeboxPlayable.Builder jukeboxPlayable(final TypedKey<JukeboxSong> song) {
        return ItemComponentTypesBridge.bridge().jukeboxPlayable(song);
    }

    @Contract(pure = true)
    JukeboxSong jukeboxSong();

    /**
     * Gets the key of the jukebox song.
     * <p>
     * Unlike {@link #jukeboxSong()}, this is available while the jukebox song
     * registry is still being loaded, such as in registry events.
     *
     * @return the key of the song
     */
    @Contract(pure = true)
    TypedKey<JukeboxSong> jukeboxSongKey();

    /**
     * Builder for {@link JukeboxPlayable}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<JukeboxPlayable> {

        /**
         * Sets the jukebox song.
         *
         * @param song the song
         * @return the builder for chaining
         * @see #jukeboxSong()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder jukeboxSong(JukeboxSong song);

        /**
         * Sets the jukebox song.
         * <p>
         * Unlike {@link #jukeboxSong(JukeboxSong)}, this can be used while the jukebox song
         * registry is still being loaded, such as in registry events.
         *
         * @param song the key of the song
         * @return the builder for chaining
         * @see #jukeboxSongKey()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder jukeboxSong(TypedKey<JukeboxSong> song);
    }
}

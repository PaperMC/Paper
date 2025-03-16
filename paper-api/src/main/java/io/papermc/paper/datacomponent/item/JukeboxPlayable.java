package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.bukkit.JukeboxSong;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the jukebox song for an item.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#JUKEBOX_PLAYABLE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface JukeboxPlayable  {

    @Contract(value = "_ -> new", pure = true)
    static JukeboxPlayable.Builder jukeboxPlayable(final JukeboxSong song) {
        return ItemComponentTypesBridge.bridge().jukeboxPlayable(song);
    }

    @Contract(pure = true)
    JukeboxSong jukeboxSong();

    /**
     * Builder for {@link JukeboxPlayable}.
     */
    @ApiStatus.Experimental
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
    }
}

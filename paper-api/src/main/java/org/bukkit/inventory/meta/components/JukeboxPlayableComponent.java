package org.bukkit.inventory.meta.components;

import org.bukkit.JukeboxSong;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a component which can be inserted into a jukebox.
 */
@ApiStatus.Experimental
public interface JukeboxPlayableComponent extends ConfigurationSerializable {

    /**
     * Gets the song assigned to this component.
     *
     * @return song, or null if the song does not exist on the server
     */
    @Nullable
    JukeboxSong getSong();

    /**
     * Gets the key of the song assigned to this component.
     *
     * @return the song key
     */
    @NotNull
    NamespacedKey getSongKey();

    /**
     * Sets the song assigned to this component.
     *
     * @param song the song
     */
    void setSong(@NotNull JukeboxSong song);

    /**
     * Sets the key of the song assigned to this component.
     *
     * @param song the song key
     */
    void setSongKey(@NotNull NamespacedKey song);

    /**
     * Gets if the song will show in the item tooltip.
     *
     * @return if the song will show in the tooltip
     * @deprecated no longer available on the component directly
     */
    @Deprecated(since = "1.21.5", forRemoval = true)
    @Contract("-> true") // todo add new item flag for compat? or just tell people to use the new data component api
    default boolean isShowInTooltip() {
        return true;
    }

    /**
     * Sets if the song will show in the item tooltip.
     *
     * @param show true if the song will show in the tooltip
     * @deprecated no longer available on the component directly
     */
    @Deprecated(since = "1.21.5", forRemoval = true)
    default void setShowInTooltip(boolean show) {
    }
}

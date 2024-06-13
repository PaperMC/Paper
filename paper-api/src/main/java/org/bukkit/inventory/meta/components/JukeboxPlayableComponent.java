package org.bukkit.inventory.meta.components;

import org.bukkit.JukeboxSong;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.ApiStatus;
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
     */
    boolean isShowInTooltip();

    /**
     * Sets if the song will show in the item tooltip.
     *
     * @param show true if the song will show in the tooltip
     */
    void setShowInTooltip(boolean show);
}

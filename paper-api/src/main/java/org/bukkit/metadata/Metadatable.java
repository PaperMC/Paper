package org.bukkit.metadata;

import java.util.List;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is implemented by all objects that can provide metadata
 * about themselves.
 */
public interface Metadatable {
    /**
     * Sets a metadata value in the implementing object's metadata store.
     *
     * @param metadataKey A unique key to identify this metadata.
     * @param newMetadataValue The metadata value to apply.
     * @throws IllegalArgumentException If value is null, or the owning plugin
     *     is null
     */
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue);

    /**
     * Returns a list of previously set metadata values from the implementing
     * object's metadata store.
     *
     * @param metadataKey the unique metadata key being sought.
     * @return A list of values, one for each plugin that has set the
     *     requested value.
     */
    @NotNull
    public List<MetadataValue> getMetadata(@NotNull String metadataKey);

    /**
     * Tests to see whether the implementing object contains the given
     * metadata value in its metadata store.
     *
     * @param metadataKey the unique metadata key being queried.
     * @return the existence of the metadataKey within subject.
     */
    public boolean hasMetadata(@NotNull String metadataKey);

    /**
     * Removes the given metadata value from the implementing object's
     * metadata store.
     *
     * @param metadataKey the unique metadata key identifying the metadata to
     *     remove.
     * @param owningPlugin This plugin's metadata value will be removed. All
     *     other values will be left untouched.
     * @throws IllegalArgumentException If plugin is null
     */
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin);
}

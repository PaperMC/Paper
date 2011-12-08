package org.bukkit.craftbukkit.metadata;

import org.bukkit.World;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;

/**
 * An WorldMetadataStore stores metadata values for {@link World} objects.
 */
public class WorldMetadataStore extends MetadataStoreBase<World> implements MetadataStore<World> {
    /**
     * Generates a unique metadata key for a {@link World} object based on the world UID.
     * @see WorldMetadataStore#disambiguate(Object, String)
     * @param world the world
     * @param metadataKey The name identifying the metadata value
     * @return a unique metadata key
     */
    @Override
    protected String disambiguate(World world, String metadataKey) {
        return world.getUID().toString() + ":" + metadataKey;
    }
}

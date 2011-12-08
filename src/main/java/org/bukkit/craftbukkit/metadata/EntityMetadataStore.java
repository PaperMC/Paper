package org.bukkit.craftbukkit.metadata;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;

/**
 * An EntityMetadataStore stores metadata values for all {@link Entity} classes an their descendants.
 */
public class EntityMetadataStore extends MetadataStoreBase<Entity> implements MetadataStore<Entity> {
    /**
     * Generates a unique metadata key for an {@link Entity} entity ID.
     * @see MetadataStoreBase#disambiguate(Object, String)
     * @param entity the entity
     * @param metadataKey The name identifying the metadata value
     * @return a unique metadata key
     */
    @Override
    protected String disambiguate(Entity entity, String metadataKey) {
        return Integer.toString(entity.getEntityId()) + ":" + metadataKey;
    }
}

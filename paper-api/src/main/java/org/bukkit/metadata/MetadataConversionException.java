package org.bukkit.metadata;

/**
 * A MetadataConversionException is thrown any time a {@link
 * LazyMetadataValue} attempts to convert a metadata value to an inappropriate
 * data type.
 *
 * @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
 * removed. It is recommended that when wanting persistent metadata, you use {@link org.bukkit.persistence.PersistentDataContainer}.
 * If you want temporary values on an entity, just use the entity lifecycle events. (See {@link com.destroystokyo.paper.event.entity.EntityAddToWorldEvent}0
 */
@Deprecated
@SuppressWarnings("serial")
public class MetadataConversionException extends RuntimeException {
    MetadataConversionException(String message) {
        super(message);
    }
}

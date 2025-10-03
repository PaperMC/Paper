package org.bukkit.metadata;

/**
 * A MetadataEvaluationException is thrown any time a {@link
 * LazyMetadataValue} fails to evaluate its value due to an exception. The
 * originating exception will be included as this exception's cause.
 *
 * @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
 * removed. It is recommended that when wanting persistent metadata, you use {@link org.bukkit.persistence.PersistentDataContainer}.
 * If you want temporary values on an entity, just use the entity lifecycle events. (See {@link com.destroystokyo.paper.event.entity.EntityAddToWorldEvent}0
 */
@Deprecated
@SuppressWarnings("serial")
public class MetadataEvaluationException extends RuntimeException {
    MetadataEvaluationException(Throwable cause) {
        super(cause);
    }
}

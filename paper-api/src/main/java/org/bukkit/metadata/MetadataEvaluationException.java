package org.bukkit.metadata;

/// A MetadataEvaluationException is thrown any time a
/// [LazyMetadataValue] fails to evaluate its value due to an exception. The
/// originating exception will be included as this exception's cause.
///
/// @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
/// removed. It is recommended that when wanting persistent metadata, you use [org.bukkit.persistence.PersistentDataContainer].
///
/// If you want temporary values on an entity, use the entity lifecycle events and a [java.util.Map] of your own. (See [com.destroystokyo.paper.event.entity.EntityAddToWorldEvent] and [com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent])
@Deprecated
@SuppressWarnings("serial")
public class MetadataEvaluationException extends RuntimeException {
    MetadataEvaluationException(Throwable cause) {
        super(cause);
    }
}

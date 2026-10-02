package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// A FixedMetadataValue is a special case metadata item that contains the same
/// value forever after initialization. Invalidating a FixedMetadataValue has
/// no effect.
///
/// This class extends LazyMetadataValue for historical reasons, even though it
/// overrides all the implementation methods. it is possible that in the future
/// that the inheritance hierarchy may change.
///
/// @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
/// removed. It is recommended that when wanting persistent metadata, you use [org.bukkit.persistence.PersistentDataContainer].
///
/// If you want temporary values on an entity, use the entity lifecycle events and a [java.util.Map] of your own. (See [com.destroystokyo.paper.event.entity.EntityAddToWorldEvent] and [com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent])
@Deprecated
public class FixedMetadataValue extends LazyMetadataValue {

    /// Store the internal value that is represented by this fixed value.
    private final Object internalValue;

    /// Initializes a FixedMetadataValue with an Object
    ///
    /// @param owningPlugin the [Plugin] that created this metadata value
    /// @param value the value assigned to this metadata value
    public FixedMetadataValue(@NotNull Plugin owningPlugin, @Nullable final Object value) {
        super(owningPlugin);
        this.internalValue = value;
    }

    @Override
    public void invalidate() {

    }

    @Nullable
    @Override
    public Object value() {
        return internalValue;
    }
}

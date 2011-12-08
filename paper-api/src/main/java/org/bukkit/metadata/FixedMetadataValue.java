package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;

/**
 * A FixedMetadataValue is a special case metadata item that contains the same value forever after initialization.
 * Invalidating a FixedMetadataValue has no affect.
 */
public class FixedMetadataValue extends LazyMetadataValue {
    /**
     * Initializes a FixedMetadataValue with an Object
     *
     * @param owningPlugin the {@link Plugin} that created this metadata value.
     * @param value the value assigned to this metadata value.
     */
    public FixedMetadataValue(Plugin owningPlugin, final Object value) {
        super(owningPlugin, CacheStrategy.CACHE_ETERNALLY, new Callable<Object>() {
            public Object call() throws Exception {
                return value;
            }
        });
    }
}

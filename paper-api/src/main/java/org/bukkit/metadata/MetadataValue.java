package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
 * removed. It is recommended that when wanting persistent metadata, you use {@link org.bukkit.persistence.PersistentDataContainer}.
 * If you want temporary values on an entity, just use the entity lifecycle events. (See {@link com.destroystokyo.paper.event.entity.EntityAddToWorldEvent}0
 */
@Deprecated
public interface MetadataValue {

    /**
     * Fetches the value of this metadata item.
     *
     * @return the metadata value.
     */
    @Nullable
    public Object value();

    /**
     * Attempts to convert the value of this metadata item into an int.
     *
     * @return the value as an int.
     */
    public int asInt();

    /**
     * Attempts to convert the value of this metadata item into a float.
     *
     * @return the value as a float.
     */
    public float asFloat();

    /**
     * Attempts to convert the value of this metadata item into a double.
     *
     * @return the value as a double.
     */
    public double asDouble();

    /**
     * Attempts to convert the value of this metadata item into a long.
     *
     * @return the value as a long.
     */
    public long asLong();

    /**
     * Attempts to convert the value of this metadata item into a short.
     *
     * @return the value as a short.
     */
    public short asShort();

    /**
     * Attempts to convert the value of this metadata item into a byte.
     *
     * @return the value as a byte.
     */
    public byte asByte();

    /**
     * Attempts to convert the value of this metadata item into a boolean.
     *
     * @return the value as a boolean.
     */
    public boolean asBoolean();

    /**
     * Attempts to convert the value of this metadata item into a string.
     *
     * @return the value as a string.
     */
    @NotNull
    public String asString();

    /**
     * Returns the {@link Plugin} that created this metadata item.
     *
     * @return the plugin that owns this metadata value. Could be null if the plugin was already unloaded.
     */
    @Nullable
    public Plugin getOwningPlugin();

    /**
     * Invalidates this metadata item, forcing it to recompute when next
     * accessed.
     */
    public void invalidate();
}

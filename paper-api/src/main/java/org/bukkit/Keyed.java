package org.bukkit;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object which has a {@link NamespacedKey} attached to it.
 *
 * @since 1.12
 */
public interface Keyed extends net.kyori.adventure.key.Keyed { // Paper -- extend Adventure Keyed

    /**
     * Return the namespaced identifier for this object.
     *
     * @return this object's key
     */
    @NotNull
    NamespacedKey getKey();

    // Paper start
    /**
     * Returns the unique identifier for this object.
     *
     * @return this object's key
     * @since 1.18.1
     */
    @Override
    default net.kyori.adventure.key.@NotNull Key key() {
        return this.getKey();
    }
    // Paper end
}

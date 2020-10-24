package org.bukkit.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an object which may contain attributes.
 */
public interface Attributable {

    /**
     * Gets the specified attribute instance from the object. This instance will
     * be backed directly to the object and any changes will be visible at once.
     *
     * @param attribute the attribute to get
     * @return the attribute instance or null if not applicable to this object
     */
    @Nullable
    AttributeInstance getAttribute(@NotNull Attribute attribute);

    // Paper start
    /**
     * Registers a generic attribute to that attributable instance.
     * Allows it to add attributes not registered by default to that entity.
     *
     * @param attribute the generic attribute to register
     */
    void registerAttribute(@NotNull Attribute attribute);
    // Paper end
}

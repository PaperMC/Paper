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

    /**
     * Checks if this object has the specified attribute registered.
     *
     * @param attribute the attribute to check
     * @return true if the attribute is registered, false otherwise
     */
    boolean hasAttribute(@NotNull Attribute attribute);

    /**
     * Gets all registered attributes for this object.
     *
     * @return a collection of all registered attributes
     */
    @NotNull
    java.util.Collection<Attribute> getAttributes();

    /**
     * Gets the base value of the specified attribute.
     *
     * @param attribute the attribute to get the base value for
     * @return the base value of the attribute, or 0 if not registered
     */
    double getBaseValue(@NotNull Attribute attribute);

    /**
     * Sets the base value of the specified attribute.
     *
     * @param attribute the attribute to set the base value for
     * @param value the new base value
     * @throws IllegalArgumentException if the attribute is not registered
     */
    void setBaseValue(@NotNull Attribute attribute, double value);

    /**
     * Gets the final value of the specified attribute after all modifiers are applied.
     *
     * @param attribute the attribute to get the final value for
     * @return the final value of the attribute, or 0 if not registered
     */
    double getValue(@NotNull Attribute attribute);
    // Paper end
}

package org.bukkit.attribute;

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
    AttributeInstance getAttribute(Attribute attribute);
}

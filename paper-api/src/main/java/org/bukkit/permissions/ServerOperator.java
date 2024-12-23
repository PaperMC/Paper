package org.bukkit.permissions;

import org.bukkit.entity.Player;

/**
 * Represents an object that may become a server operator, such as a {@link
 * Player}
 *
 * @since 1.0.0
 */
public interface ServerOperator {

    /**
     * Checks if this object is a server operator
     *
     * @return true if this is an operator, otherwise false
     */
    public boolean isOp();

    /**
     * Sets the operator status of this object
     *
     * @param value New operator value
     */
    public void setOp(boolean value);
}

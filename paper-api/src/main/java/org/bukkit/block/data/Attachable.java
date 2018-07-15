package org.bukkit.block.data;

/**
 * 'attached' denotes whether a tripwire hook or string forms a complete
 * tripwire circuit and is ready to trigger.
 * <br>
 * Updating the property on a tripwire hook will change the texture to indicate
 * a connected string, but will not have any effect when used on the tripwire
 * string itself. It may however still be used to check whether the string forms
 * a circuit.
 */
public interface Attachable extends BlockData {

    /**
     * Gets the value of the 'attached' property.
     *
     * @return the 'attached' value
     */
    boolean isAttached();

    /**
     * Sets the value of the 'attached' property.
     *
     * @param attached the new 'attached' value
     */
    void setAttached(boolean attached);
}

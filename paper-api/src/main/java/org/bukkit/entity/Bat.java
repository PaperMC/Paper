package org.bukkit.entity;

/**
 * Represents a Bat
 */
public interface Bat extends Ambient {

    /**
     * Checks the current waking state of this bat.
     * <p>
     * This does not imply any persistence of state past the method call.
     *
     * @return true if the bat is awake or false if it is currently hanging
     *     from a block
     */
    boolean isAwake();

    /**
     * This method modifies the current waking state of this bat.
     * <p>
     * This does not prevent a bat from spontaneously awaking itself, or from
     * reattaching itself to a block.
     *
     * @param state the new state
     */
    void setAwake(boolean state);
}

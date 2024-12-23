package org.bukkit.entity;

/**
 * An iron Golem that protects Villages.
 *
 * @since 1.2.2
 */
public interface IronGolem extends Golem {

    /**
     * Gets whether this iron golem was built by a player.
     *
     * @return Whether this iron golem was built by a player
     * @since 1.3.1
     */
    public boolean isPlayerCreated();

    /**
     * Sets whether this iron golem was built by a player or not.
     *
     * @param playerCreated true if you want to set the iron golem as being
     *     player created, false if you want it to be a natural village golem.
     * @since 1.3.1
     */
    public void setPlayerCreated(boolean playerCreated);
}

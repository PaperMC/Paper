package org.bukkit.entity;

/**
 * Represents an instance of a lightning strike. May or may not do damage.
 *
 * @author sk89q
 */
public interface LightningStrike extends Weather {

    /**
     * Returns whether the strike is an effect that does no damage.
     *
     * @return whether the strike is an effect
     */
    public boolean isEffect();

}

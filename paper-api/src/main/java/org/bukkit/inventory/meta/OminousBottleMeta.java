package org.bukkit.inventory.meta;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an ominous bottle with an amplifier of the bad omen effect.
 */
public interface OminousBottleMeta extends ItemMeta {

    /**
     * Checks for the presence of an amplifier.
     *
     * @return true if a customer amplifier is applied
     */
    boolean hasAmplifier();

    /**
     * Gets the amplifier amount for an Ominous Bottle's bad omen effect.
     * <p>
     * Plugins should check that hasAmplifier() returns true before calling this
     * method.
     *
     * @return amplifier
     */
    int getAmplifier();

    /**
     * Sets the amplifier amount for an Ominous Bottle's bad omen effect.
     *
     * @param amplifier between 0 and 4
     */
    void setAmplifier(int amplifier);

    @Override
    @NotNull
    OminousBottleMeta clone();
}

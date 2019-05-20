package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents the different types of wooden steps.
 *
 * @see Material#LEGACY_WOOD_STEP
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class WoodenStep extends Wood {
    protected static final Material DEFAULT_TYPE = Material.LEGACY_WOOD_STEP;
    protected static final boolean DEFAULT_INVERTED = false;

    /**
     * Constructs a wooden step.
     */
    public WoodenStep() {
        this(DEFAULT_SPECIES, DEFAULT_INVERTED);
    }

    /**
     * Constructs a wooden step of the given tree species.
     *
     * @param species the species of the wooden step
     */
    public WoodenStep(TreeSpecies species) {
        this(species, DEFAULT_INVERTED);
    }

    /**
     * Constructs a wooden step of the given type and tree species, either
     * inverted or not.
     *
     * @param species the species of the wooden step
     * @param inv true the step is at the top of the block
     */
    public WoodenStep(final TreeSpecies species, boolean inv) {
        super(DEFAULT_TYPE, species);
        setInverted(inv);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public WoodenStep(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Test if step is inverted
     *
     * @return true if inverted (top half), false if normal (bottom half)
     */
    @SuppressWarnings("deprecation")
    public boolean isInverted() {
        return ((getData() & 0x8) != 0);
    }

    /**
     * Set step inverted state
     *
     * @param inv - true if step is inverted (top half), false if step is normal
     * (bottom half)
     */
    @SuppressWarnings("deprecation")
    public void setInverted(boolean inv) {
        int dat = getData() & 0x7;
        if (inv) {
            dat |= 0x8;
        }
        setData((byte) dat);
    }

    @Override
    public WoodenStep clone() {
        return (WoodenStep) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + " " + getSpecies() + (isInverted() ? " inverted" : "");
    }
}

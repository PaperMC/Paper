package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents the different types of wooden steps.
 */
public class WoodenStep extends MaterialData {

    public WoodenStep() {
        super(Material.WOOD_STEP);
    }

    public WoodenStep(final int type) {
        super(type);
    }

    public WoodenStep(TreeSpecies species) {
        this();
        setSpecies(species);
    }

    public WoodenStep(TreeSpecies species, boolean inv) {
        this();
        setSpecies(species);
        setInverted(inv);
    }

    public WoodenStep(final int type, final byte data) {
        super(type, data);
    }

    public WoodenStep(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current species of this tree
     *
     * @return TreeSpecies of this tree
     */
    public TreeSpecies getSpecies() {
        return TreeSpecies.getByData((byte) (getData() & 0x3));
    }

    /**
     * Sets the species of this tree
     *
     * @param species New species of this tree
     */
    public void setSpecies(TreeSpecies species) {
        setData((byte) ((getData() & 0xC) | species.getData()));
    }

    /**
     * Test if step is inverted
     * @return true if inverted (top half), false if normal (bottom half)
     */
    public boolean isInverted() {
        return ((getData() & 0x8) != 0);
    }
    
    /**
     * Set step inverted state
     * @param inv - true if step is inverted (top half), false if step is normal (bottom half)
     */
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
        return super.toString() + " " + getSpecies() + (isInverted()?" inverted":"");
    }
}

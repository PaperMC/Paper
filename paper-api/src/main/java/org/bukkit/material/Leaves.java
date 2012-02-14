package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents the different types of leaves.
 */
public class Leaves extends MaterialData {
    public Leaves() {
        super(Material.LEAVES);
    }

    public Leaves(TreeSpecies species) {
        this();
        setSpecies(species);
    }

    public Leaves(final int type) {
        super(type);
    }

    public Leaves(final Material type) {
        super(type);
    }

    public Leaves(final int type, final byte data) {
        super(type, data);
    }

    public Leaves(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current species of this leave
     *
     * @return TreeSpecies of this leave
     */
    public TreeSpecies getSpecies() {
        return TreeSpecies.getByData((byte) (getData() & 3));
    }

    /**
     * Sets the species of this leave
     *
     * @param species New species of this leave
     */
    public void setSpecies(TreeSpecies species) {
        setData(species.getData());
    }

    @Override
    public String toString() {
        return getSpecies() + " " + super.toString();
    }

    @Override
    public Leaves clone() {
        return (Leaves) super.clone();
    }
}

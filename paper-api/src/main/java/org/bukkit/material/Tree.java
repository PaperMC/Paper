package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents the different types of Trees.
 * @author sunkid
 */
public class Tree extends MaterialData {
    public Tree(final int type) {
        super(type);
    }

    public Tree(final Material type) {
        super(type);
    }

    public Tree(final int type, final byte data) {
        super(type, data);
    }

    public Tree(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current species of this tree
     *
     * @return TreeSpecies of this tree
     */
    public TreeSpecies getSpecies() {
        return TreeSpecies.getByData(getData());
    }

    /**
     * Sets the species of this tree
     *
     * @param species New species of this tree
     */
    public void setSpecies(TreeSpecies species) {
        setData(species.getData());
    }
}

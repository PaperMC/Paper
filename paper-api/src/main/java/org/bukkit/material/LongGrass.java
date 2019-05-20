package org.bukkit.material;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;

/**
 * Represents the different types of long grasses.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class LongGrass extends MaterialData {
    public LongGrass() {
        super(Material.LEGACY_LONG_GRASS);
    }

    public LongGrass(GrassSpecies species) {
        this();
        setSpecies(species);
    }

    public LongGrass(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public LongGrass(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current species of this grass
     *
     * @return GrassSpecies of this grass
     */
    public GrassSpecies getSpecies() {
        return GrassSpecies.getByData(getData());
    }

    /**
     * Sets the species of this grass
     *
     * @param species New species of this grass
     */
    public void setSpecies(GrassSpecies species) {
        setData(species.getData());
    }

    @Override
    public String toString() {
        return getSpecies() + " " + super.toString();
    }

    @Override
    public LongGrass clone() {
        return (LongGrass) super.clone();
    }
}

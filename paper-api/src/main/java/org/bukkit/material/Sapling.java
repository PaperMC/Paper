package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents the different types of Tree block that face a direction.
 *
 * @see Material#LEGACY_SAPLING
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Sapling extends Wood {

    /**
     * Constructs a sapling.
     */
    public Sapling() {
        this(DEFAULT_SPECIES);
    }

    /**
     * Constructs a sapling of the given tree species.
     *
     * @param species the species of the sapling
     */
    public Sapling(TreeSpecies species) {
        this(species, false);
    }

    /**
     * Constructs a sapling of the given tree species and if is it instant
     * growable
     *
     * @param species the species of the tree block
     * @param isInstantGrowable true if the Sapling should grow when next ticked with bonemeal
     */
    public Sapling(TreeSpecies species, boolean isInstantGrowable) {
        this(Material.LEGACY_SAPLING, species, isInstantGrowable);
    }

    /**
     * Constructs a sapling of the given type.
     *
     * @param type the type of tree block
     */
    public Sapling(final Material type) {
        this(type, DEFAULT_SPECIES, false);
    }

    /**
     * Constructs a sapling of the given type and tree species.
     *
     * @param type the type of sapling
     * @param species the species of the sapling
     */
    public Sapling(final Material type, TreeSpecies species) {
        this(type, species, false);
    }

    /**
     * Constructs a sapling of the given type and tree species and if is it
     * instant growable
     *
     * @param type the type of sapling
     * @param species the species of the sapling
     * @param isInstantGrowable true if the Sapling should grow when next ticked
     * with bonemeal
     */
    public Sapling(final Material type, TreeSpecies species, boolean isInstantGrowable) {
        super(type, species);
        setIsInstantGrowable(isInstantGrowable);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Sapling(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Checks if the Sapling would grow when next ticked with bonemeal
     *
     * @return true if the Sapling would grow when next ticked with bonemeal
     */
    public boolean isInstantGrowable() {
        return (getData() & 0x8) == 0x8;
    }

    /**
     * Set whether this sapling will grow when next ticked with bonemeal
     *
     * @param isInstantGrowable true if the Sapling should grow when next ticked
     * with bonemeal
     */
    public void setIsInstantGrowable(boolean isInstantGrowable) {
        setData(isInstantGrowable ? (byte) ((getData() & 0x7) | 0x8) : (byte) (getData() & 0x7));
    }

    @Override
    public String toString() {
        return getSpecies() + " " + (isInstantGrowable() ? " IS_INSTANT_GROWABLE " : "") + " " + super.toString();
    }

    @Override
    public Sapling clone() {
        return (Sapling) super.clone();
    }
}

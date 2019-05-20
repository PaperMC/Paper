package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents the different types of leaf block that may be permanent or can
 * decay when too far from a log.
 *
 * @see Material#LEGACY_LEAVES
 * @see Material#LEGACY_LEAVES_2
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Leaves extends Wood {
    protected static final Material DEFAULT_TYPE = Material.LEGACY_LEAVES;
    protected static final boolean DEFAULT_DECAYABLE = true;

    /**
     * Constructs a leaf block.
     */
    public Leaves() {
        this(DEFAULT_TYPE, DEFAULT_SPECIES, DEFAULT_DECAYABLE);
    }

    /**
     * Constructs a leaf block of the given tree species.
     *
     * @param species the species of the wood block
     */
    public Leaves(TreeSpecies species) {
        this(DEFAULT_TYPE, species, DEFAULT_DECAYABLE);
    }

    /**
     * Constructs a leaf block of the given tree species and flag for whether
     * this leaf block will disappear when too far from a log.
     *
     * @param species the species of the wood block
     * @param isDecayable whether the block is permanent or can disappear
     */
    public Leaves(TreeSpecies species, boolean isDecayable) {
        this(DEFAULT_TYPE, species, isDecayable);
    }

    /**
     * Constructs a leaf block of the given type.
     *
     * @param type the type of leaf block
     */
    public Leaves(final Material type) {
        this(type, DEFAULT_SPECIES, DEFAULT_DECAYABLE);
    }

    /**
     * Constructs a leaf block of the given type and tree species.
     *
     * @param type the type of leaf block
     * @param species the species of the wood block
     */
    public Leaves(final Material type, TreeSpecies species) {
        this(type, species, DEFAULT_DECAYABLE);
    }

    /**
     * Constructs a leaf block of the given type and tree species and flag for
     * whether this leaf block will disappear when too far from a log.
     *
     * @param type the type of leaf block
     * @param species the species of the wood block
     * @param isDecayable whether the block is permanent or can disappear
     */
    public Leaves(final Material type, TreeSpecies species, boolean isDecayable) {
        super(type, species);
        setDecayable(isDecayable);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Leaves(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Checks if this leaf block is in the process of decaying
     *
     * @return true if the leaf block is in the process of decaying
     */
    public boolean isDecaying() {
        return (getData() & 0x8) != 0;
    }

    /**
     * Set whether this leaf block is in the process of decaying
     *
     * @param isDecaying whether the block is decaying or not
     */
    public void setDecaying(boolean isDecaying) {
        setData((byte) ((getData() & 0x3) | (isDecaying
                ? 0x8 // Clear the permanent flag to make this a decayable flag and set the decaying flag
                : (getData() & 0x4)))); // Only persist the decayable flag if this is not a decaying block
    }

    /**
     * Checks if this leaf block is permanent or can decay when too far from a
     * log
     *
     * @return true if the leaf block is permanent or can decay when too far
     * from a log
     */
    public boolean isDecayable() {
        return (getData() & 0x4) == 0;
    }

    /**
     * Set whether this leaf block will disappear when too far from a log
     *
     * @param isDecayable whether the block is permanent or can disappear
     */
    public void setDecayable(boolean isDecayable) {
        setData((byte) ((getData() & 0x3) | (isDecayable
                ? (getData() & 0x8) // Only persist the decaying flag if this is a decayable block
                : 0x4)));
    }

    @Override
    public String toString() {
        return getSpecies() + (isDecayable() ? " DECAYABLE " : " PERMANENT ") + (isDecaying() ? " DECAYING " : " ") + super.toString();
    }

    @Override
    public Leaves clone() {
        return (Leaves) super.clone();
    }
}

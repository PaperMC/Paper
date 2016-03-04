package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents wood blocks of different species.
 *
 * @see Material#WOOD
 * @see Material#SAPLING
 * @see Material#WOOD_DOUBLE_STEP
 */
public class Wood extends MaterialData {
    protected static final Material DEFAULT_TYPE = Material.WOOD;
    protected static final TreeSpecies DEFAULT_SPECIES = TreeSpecies.GENERIC;

    /**
     * Constructs a wood block.
     */
    public Wood() {
        this(DEFAULT_TYPE, DEFAULT_SPECIES);
    }

    /**
     * Constructs a wood block of the given tree species.
     * 
     * @param species the species of the wood block
     */
    public Wood(TreeSpecies species) {
        this(DEFAULT_TYPE, species);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public Wood(final int type) {
        super(type);
    }

    /**
     * Constructs a wood block of the given type.
     *
     * @param type the type of wood block
     */
    public Wood(final Material type) {
        this(type, DEFAULT_SPECIES);
    }

    /**
     * Constructs a wood block of the given type and tree species.
     *
     * @param type the type of wood block
     * @param species the species of the wood block
     */
    public Wood(final Material type, final TreeSpecies species) {
        // Ensure only valid species-type combinations
        super(getSpeciesType(type, species));
        setSpecies(species);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Wood(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Wood(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current species of this wood block
     *
     * @return TreeSpecies of this wood block
     */
    public TreeSpecies getSpecies() {
        switch (getItemType()) {
            case WOOD:
            case WOOD_DOUBLE_STEP:
                return TreeSpecies.getByData((byte) getData());
            case LOG:
            case LEAVES:
                return TreeSpecies.getByData((byte) (getData() & 0x3));
            case LOG_2:
            case LEAVES_2:
                return TreeSpecies.getByData((byte) ((getData() & 0x3) | 0x4));
            case SAPLING:
            case WOOD_STEP:
                return TreeSpecies.getByData((byte) (getData() & 0x7));
            default:
                throw new IllegalArgumentException("Invalid block type for tree species");
        }
    }

    /**
     * Correct the block type for certain species-type combinations.
     *
     * @param type The desired type
     * @param species The required species
     * @return The actual type for this species given the desired type
     */
    private static Material getSpeciesType(Material type, TreeSpecies species) {
        switch (species) {
            case GENERIC:
            case REDWOOD:
            case BIRCH:
            case JUNGLE:
                switch (type) {
                    case LOG_2:
                        return Material.LOG;
                    case LEAVES_2:
                        return Material.LEAVES;
                    default:
                }
                break;
            case ACACIA:
            case DARK_OAK:
                switch (type) {
                    case LOG:
                        return Material.LOG_2;
                    case LEAVES:
                        return Material.LEAVES_2;
                    default:
                }
                break;
        }
        return type;
    }

    /**
     * Sets the species of this wood block
     *
     * @param species New species of this wood block
     */
    public void setSpecies(final TreeSpecies species) {
        boolean firstType = false;
        switch (getItemType()) {
            case WOOD:
            case WOOD_DOUBLE_STEP:
                setData(species.getData());
                break;
            case LOG:
            case LEAVES:
                firstType = true;
            // fall through to next switch statement below
            case LOG_2:
            case LEAVES_2:
                switch (species) {
                    case GENERIC:
                    case REDWOOD:
                    case BIRCH:
                    case JUNGLE:
                        if (!firstType) {
                            throw new IllegalArgumentException("Invalid tree species for block type, use block type 2 instead");
                        }
                        break;
                    case ACACIA:
                    case DARK_OAK:
                        if (firstType) {
                            throw new IllegalArgumentException("Invalid tree species for block type 2, use block type instead");
                        }
                        break;
                }
                setData((byte) ((getData() & 0xC) | (species.getData() & 0x3)));
                break;
            case SAPLING:
            case WOOD_STEP:
                setData((byte) ((getData() & 0x8) | species.getData()));
                break;
            default:
                throw new IllegalArgumentException("Invalid block type for tree species");
        }
    }

    @Override
    public String toString() {
        return getSpecies() + " " + super.toString();
    }

    @Override
    public Wood clone() {
        return (Wood) super.clone();
    }
}

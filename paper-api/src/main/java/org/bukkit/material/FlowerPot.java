package org.bukkit.material;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;

/**
 * Represents a flower pot.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class FlowerPot extends MaterialData {

    /**
     * Default constructor for a flower pot.
     */
    public FlowerPot() {
        super(Material.LEGACY_FLOWER_POT);
    }

    public FlowerPot(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public FlowerPot(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Get the material in the flower pot
     *
     * @return material MaterialData for the block currently in the flower pot
     *     or null if empty
     */
    public MaterialData getContents() {
        switch (getData()) {
            case 1:
                return new MaterialData(Material.LEGACY_RED_ROSE);
            case 2:
                return new MaterialData(Material.LEGACY_YELLOW_FLOWER);
            case 3:
                return new Tree(TreeSpecies.GENERIC);
            case 4:
                return new Tree(TreeSpecies.REDWOOD);
            case 5:
                return new Tree(TreeSpecies.BIRCH);
            case 6:
                return new Tree(TreeSpecies.JUNGLE);
            case 7:
                return new MaterialData(Material.LEGACY_RED_MUSHROOM);
            case 8:
                return new MaterialData(Material.LEGACY_BROWN_MUSHROOM);
            case 9:
                return new MaterialData(Material.LEGACY_CACTUS);
            case 10:
                return new MaterialData(Material.LEGACY_DEAD_BUSH);
            case 11:
                return new LongGrass(GrassSpecies.FERN_LIKE);
            default:
                return null;
        }
    }

    /**
     * Set the contents of the flower pot
     *
     * @param materialData MaterialData of the block to put in the flower pot.
     */
    public void setContents(MaterialData materialData) {
        Material mat = materialData.getItemType();

        if (mat == Material.LEGACY_RED_ROSE) {
            setData((byte) 1);
        } else if (mat == Material.LEGACY_YELLOW_FLOWER) {
            setData((byte) 2);
        } else if (mat == Material.LEGACY_RED_MUSHROOM) {
            setData((byte) 7);
        } else if (mat == Material.LEGACY_BROWN_MUSHROOM) {
            setData((byte) 8);
        } else if (mat == Material.LEGACY_CACTUS) {
            setData((byte) 9);
        } else if (mat == Material.LEGACY_DEAD_BUSH) {
            setData((byte) 10);
        } else if (mat == Material.LEGACY_SAPLING) {
            TreeSpecies species = ((Tree) materialData).getSpecies();

            if (species == TreeSpecies.GENERIC) {
                setData((byte) 3);
            } else if (species == TreeSpecies.REDWOOD) {
                setData((byte) 4);
            } else if (species == TreeSpecies.BIRCH) {
                setData((byte) 5);
            } else {
                setData((byte) 6);
            }
        } else if (mat == Material.LEGACY_LONG_GRASS) {
            GrassSpecies species = ((LongGrass) materialData).getSpecies();

            if (species == GrassSpecies.FERN_LIKE) {
                setData((byte) 11);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " containing " + getContents();
    }

    @Override
    public FlowerPot clone() {
        return (FlowerPot) super.clone();
    }
}

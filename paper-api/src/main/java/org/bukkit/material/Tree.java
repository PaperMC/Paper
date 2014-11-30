package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;

/**
 * Represents the different types of Trees.
 */
public class Tree extends MaterialData {
    public Tree() {
        super(Material.LOG);
    }

    public Tree(TreeSpecies species) {
        this();
        setSpecies(species);
    }

    public Tree(TreeSpecies species, BlockFace dir) {
        this();
        setSpecies(species);
        setDirection(dir);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public Tree(final int type) {
        super(type);
    }

    public Tree(final Material type) {
        super(type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Tree(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Tree(final Material type, final byte data) {
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
     * Get direction of the log
     *
     * @return one of:
     *     <ul>
     *     <li>BlockFace.TOP for upright (default)
     *     <li>BlockFace.NORTH (east-west)
     *     <li>BlockFace.WEST (north-south)
     *     <li>BlockFace.SELF (directionless)
     *     </ul>
     */
    public BlockFace getDirection() {
        switch ((getData() >> 2) & 0x3) {
            case 0: // Up-down
            default:
                return BlockFace.UP;
            case 1: // North-south
                return BlockFace.WEST;
            case 2: // East-west
                return BlockFace.NORTH;
            case 3: // Directionless (bark on all sides)
                return BlockFace.SELF;
        }
    }
    /**
     * Set direction of the log
     *
     * @param dir - direction of end of log (BlockFace.SELF for no direction)
     */
    public void setDirection(BlockFace dir) {
        int dat;
        switch (dir) {
            case UP:
            case DOWN:
            default:
                dat = 0;
                break;
            case WEST:
            case EAST:
                dat = 1;
                break;
            case NORTH:
            case SOUTH:
                dat = 2;
                break;
            case SELF:
                dat = 3;
                break;
        }
        setData((byte) ((getData() & 0x3) | (dat << 2)));
    }

    @Override
    public String toString() {
        return getSpecies() + " " + getDirection() + " " + super.toString();
    }

    @Override
    public Tree clone() {
        return (Tree) super.clone();
    }
}

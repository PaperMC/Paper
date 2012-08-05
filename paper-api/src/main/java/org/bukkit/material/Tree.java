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
     * @return BlockFace.TOP for upright (default), BlockFace.EAST (east-west), BlockFace.NORTH (north-sout), BlockFace.SELF (directionless)
     */
    public BlockFace getDirection() {
        switch ((getData() >> 2) & 0x3) {
            case 0: // Up-down
            default:
                return BlockFace.UP;
            case 1: // North-south
                return BlockFace.NORTH;
            case 2: // East-west
                return BlockFace.EAST;
            case 3: // Directionless (bark on all sides)
                return BlockFace.SELF;
        }
    }
    /**
     * Set direction of the log
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
            case NORTH:
            case SOUTH:
                dat = 1;
                break;
            case EAST:
            case WEST:
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

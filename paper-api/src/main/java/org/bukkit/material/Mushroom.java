package org.bukkit.material;

import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a huge mushroom block
 */
public class Mushroom extends MaterialData {
    private static final byte SHROOM_NONE = 0;
    private static final byte SHROOM_STEM = 10;
    private static final byte NORTH_LIMIT = 4;
    private static final byte SOUTH_LIMIT = 6;
    private static final byte EAST_WEST_LIMIT = 3;
    private static final byte EAST_REMAINDER = 0;
    private static final byte WEST_REMAINDER = 1;
    private static final byte NORTH_SOUTH_MOD = 3;
    private static final byte EAST_WEST_MOD = 1;

    public Mushroom(Material shroom) {
        super(shroom);
        Validate.isTrue(shroom == Material.HUGE_MUSHROOM_1 || shroom == Material.HUGE_MUSHROOM_2, "Not a mushroom!");
    }

    /**
     * @param shroom the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Mushroom(Material shroom, byte data) {
        super(shroom, data);
        Validate.isTrue(shroom == Material.HUGE_MUSHROOM_1 || shroom == Material.HUGE_MUSHROOM_2, "Not a mushroom!");
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Mushroom(int type, byte data){
        super(type, data);
        Validate.isTrue(type == Material.HUGE_MUSHROOM_1.getId() || type == Material.HUGE_MUSHROOM_2.getId(), "Not a mushroom!");
    }

    /**
     * @return Whether this is a mushroom stem.
     */
    public boolean isStem() {
        return getData() == SHROOM_STEM;
    }

    /**
     * Sets this to be a mushroom stem.
     */
    public void setStem() {
        setData((byte) 10);
    }

    /**
     * Checks whether a face of the block is painted.
     *
     * @param face The face to check.
     * @return True if it is painted.
     */
    public boolean isFacePainted(BlockFace face) {
        byte data = getData();

        if (data == SHROOM_NONE || data == SHROOM_STEM) {
            return false;
        }

        switch (face) {
            case WEST:
                return data < NORTH_LIMIT;
            case EAST:
                return data > SOUTH_LIMIT;
            case NORTH:
                return data % EAST_WEST_LIMIT == EAST_REMAINDER;
            case SOUTH:
                return data % EAST_WEST_LIMIT == WEST_REMAINDER;
            case UP:
                return true;
            default:
                return false;
        }
    }

    /**
     * Set a face of the block to be painted or not. Note that due to the
     * nature of how the data is stored, setting a face painted or not is not
     * guaranteed to leave the other faces unchanged.
     *
     * @param face The face to paint or unpaint.
     * @param painted True if you want to paint it, false if you want the
     *     pores to show.
     */
    public void setFacePainted(BlockFace face, boolean painted) {
        if (painted == isFacePainted(face)) {
            return;
        }

        byte data = getData();

        if (data == SHROOM_STEM) {
            data = 5;
        }

        switch (face) {
            case WEST:
                if (painted) {
                    data -= NORTH_SOUTH_MOD;
                } else {
                    data += NORTH_SOUTH_MOD;
                }

                break;
            case EAST:
                if (painted) {
                    data += NORTH_SOUTH_MOD;
                } else {
                    data -= NORTH_SOUTH_MOD;
                }

                break;
            case NORTH:
                if (painted) {
                    data += EAST_WEST_MOD;
                } else {
                    data -= EAST_WEST_MOD;
                }

                break;
            case SOUTH:
                if (painted) {
                    data -= EAST_WEST_MOD;
                } else {
                    data += EAST_WEST_MOD;
                }

                break;
            case UP:
                if (!painted) {
                    data = 0;
                }

                break;
            default:
                throw new IllegalArgumentException("Can't paint that face of a mushroom!");
        }

        setData(data);
    }

    /**
     * @return A set of all faces that are currently painted (an empty set if
     *     it is a stem)
     */
    public Set<BlockFace> getPaintedFaces() {
        EnumSet<BlockFace> faces = EnumSet.noneOf(BlockFace.class);

        if (isFacePainted(BlockFace.WEST)) {
            faces.add(BlockFace.WEST);
        }

        if (isFacePainted(BlockFace.NORTH)) {
            faces.add(BlockFace.NORTH);
        }

        if (isFacePainted(BlockFace.SOUTH)) {
            faces.add(BlockFace.SOUTH);
        }

        if (isFacePainted(BlockFace.EAST)) {
            faces.add(BlockFace.EAST);
        }

        if (isFacePainted(BlockFace.UP)) {
            faces.add(BlockFace.UP);
        }

        return faces;
    }

    @Override
    public String toString() {
        return Material.getMaterial(getItemTypeId()).toString() + (isStem() ? "{STEM}" : getPaintedFaces());
    }

    @Override
    public Mushroom clone() {
        return (Mushroom) super.clone();
    }
}

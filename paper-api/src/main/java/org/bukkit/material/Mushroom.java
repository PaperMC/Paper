package org.bukkit.material;

import com.google.common.base.Preconditions;
import java.util.EnumSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.types.MushroomBlockTexture;

/**
 * Represents a huge mushroom block with certain combinations of faces set to
 * cap, pores or stem.
 *
 * @see Material#LEGACY_HUGE_MUSHROOM_1
 * @see Material#LEGACY_HUGE_MUSHROOM_2
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Mushroom extends MaterialData {
    private static final byte NORTH_LIMIT = 4;
    private static final byte SOUTH_LIMIT = 6;
    private static final byte EAST_WEST_LIMIT = 3;
    private static final byte EAST_REMAINDER = 0;
    private static final byte WEST_REMAINDER = 1;
    private static final byte NORTH_SOUTH_MOD = 3;
    private static final byte EAST_WEST_MOD = 1;

    /**
     * Constructs a brown/red mushroom block with all sides set to pores.
     *
     * @param shroom A brown or red mushroom material type.
     *
     * @see Material#LEGACY_HUGE_MUSHROOM_1
     * @see Material#LEGACY_HUGE_MUSHROOM_2
     */
    public Mushroom(Material shroom) {
        super(shroom);
        Preconditions.checkArgument(shroom == Material.LEGACY_HUGE_MUSHROOM_1 || shroom == Material.LEGACY_HUGE_MUSHROOM_2, "Not a mushroom!");
    }

    /**
     * Constructs a brown/red mushroom cap block with the specified face or
     * faces set to cap texture.
     *
     * Setting any of the four sides will also set the top to cap.
     *
     * To set two side faces at once use e.g. north-west.
     *
     * Specify self to set all six faces at once.
     *
     * @param shroom A brown or red mushroom material type.
     * @param capFace The face or faces to set to mushroom cap texture.
     *
     * @see Material#LEGACY_HUGE_MUSHROOM_1
     * @see Material#LEGACY_HUGE_MUSHROOM_2
     * @see BlockFace
     */
    public Mushroom(Material shroom, BlockFace capFace) {
        this(shroom, MushroomBlockTexture.getCapByFace(capFace));
    }

    /**
     * Constructs a brown/red mushroom block with the specified textures.
     *
     * @param shroom A brown or red mushroom material type.
     * @param texture The textured mushroom faces.
     *
     * @see Material#LEGACY_HUGE_MUSHROOM_1
     * @see Material#LEGACY_HUGE_MUSHROOM_2
     */
    public Mushroom(Material shroom, MushroomBlockTexture texture) {
        this(shroom, texture.getData());
    }

    /**
     * @param shroom the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Mushroom(Material shroom, byte data) {
        super(shroom, data);
        Preconditions.checkArgument(shroom == Material.LEGACY_HUGE_MUSHROOM_1 || shroom == Material.LEGACY_HUGE_MUSHROOM_2, "Not a mushroom!");
    }

    /**
     * @return Whether this is a mushroom stem.
     */
    public boolean isStem() {
        return getData() == MushroomBlockTexture.STEM_SIDES.getData() || getData() == MushroomBlockTexture.ALL_STEM.getData();
    }

    /**
     * Sets this to be a mushroom stem.
     *
     * @see MushroomBlockTexture#STEM_SIDES
     * @see MushroomBlockTexture#ALL_STEM
     *
     * @deprecated Use
     * {@link #setBlockTexture(org.bukkit.material.types.MushroomBlockTexture)}
     * with {@link MushroomBlockTexture#STEM_SIDES } or
     * {@link MushroomBlockTexture#ALL_STEM}
     */
    @Deprecated
    public void setStem() {
        setData((byte) MushroomBlockTexture.STEM_SIDES.getData());
    }

    /**
     * Gets the mushroom texture of this block.
     *
     * @return The mushroom texture of this block
     */
    public MushroomBlockTexture getBlockTexture() {
        return MushroomBlockTexture.getByData(getData());
    }

    /**
     * Sets the mushroom texture of this block.
     *
     * @param texture The mushroom texture to set
     */
    public void setBlockTexture(MushroomBlockTexture texture) {
        setData(texture.getData());
    }

    /**
     * Checks whether a face of the block is painted with cap texture.
     *
     * @param face The face to check.
     * @return True if it is painted.
     */
    public boolean isFacePainted(BlockFace face) {
        byte data = getData();

        if (data == MushroomBlockTexture.ALL_PORES.getData() || data == MushroomBlockTexture.STEM_SIDES.getData()
                || data == MushroomBlockTexture.ALL_STEM.getData()) {
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
            case DOWN:
            case SELF:
                return data == MushroomBlockTexture.ALL_CAP.getData();
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
     *
     * @deprecated Use MushroomBlockType cap options
     */
    @Deprecated
    public void setFacePainted(BlockFace face, boolean painted) {
        if (painted == isFacePainted(face)) {
            return;
        }

        byte data = getData();

        if (data == MushroomBlockTexture.ALL_PORES.getData() || isStem()) {
            data = MushroomBlockTexture.CAP_TOP.getData();
        }
        if (data == MushroomBlockTexture.ALL_CAP.getData() && !painted) {
            data = MushroomBlockTexture.CAP_TOP.getData();
            face = face.getOppositeFace();
            painted = true;
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
                    data = MushroomBlockTexture.ALL_PORES.getData();
                }
                break;
            case SELF:
            case DOWN:
                if (painted) {
                    data = MushroomBlockTexture.ALL_CAP.getData();
                } else {
                    data = MushroomBlockTexture.ALL_PORES.getData();
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

        if (isFacePainted(BlockFace.DOWN)) {
            faces.add(BlockFace.DOWN);
        }

        return faces;
    }

    @Override
    public String toString() {
        return getItemType() + (isStem() ? " STEM " : " CAP ") + getPaintedFaces();
    }

    @Override
    public Mushroom clone() {
        return (Mushroom) super.clone();
    }
}

package org.bukkit.material.types;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the different textured blocks of mushroom.
 */
public enum MushroomBlockTexture {

    /**
     * Pores on all faces.
     */
    ALL_PORES(0, null),
    /**
     * Cap texture on the top, north and west faces, pores on remaining sides.
     */
    CAP_NORTH_WEST(1, BlockFace.NORTH_WEST),
    /**
     * Cap texture on the top and north faces, pores on remaining sides.
     */
    CAP_NORTH(2, BlockFace.NORTH),
    /**
     * Cap texture on the top, north and east faces, pores on remaining sides.
     */
    CAP_NORTH_EAST(3, BlockFace.NORTH_EAST),
    /**
     * Cap texture on the top and west faces, pores on remaining sides.
     */
    CAP_WEST(4, BlockFace.WEST),
    /**
     * Cap texture on the top face, pores on remaining sides.
     */
    CAP_TOP(5, BlockFace.UP),
    /**
     * Cap texture on the top and east faces, pores on remaining sides.
     */
    CAP_EAST(6, BlockFace.EAST),
    /**
     * Cap texture on the top, south and west faces, pores on remaining sides.
     */
    CAP_SOUTH_WEST(7, BlockFace.SOUTH_WEST),
    /**
     * Cap texture on the top and south faces, pores on remaining sides.
     */
    CAP_SOUTH(8, BlockFace.SOUTH),
    /**
     * Cap texture on the top, south and east faces, pores on remaining sides.
     */
    CAP_SOUTH_EAST(9, BlockFace.SOUTH_EAST),
    /**
     * Stem texture on the north, east, south and west faces, pores on top and
     * bottom.
     */
    STEM_SIDES(10, null),
    /**
     * Cap texture on all faces.
     */
    ALL_CAP(14, BlockFace.SELF),
    /**
     * Stem texture on all faces.
     */
    ALL_STEM(15, null);
    private static final Map<Byte, MushroomBlockTexture> BY_DATA = Maps.newHashMap();
    private static final Map<BlockFace, MushroomBlockTexture> BY_BLOCKFACE = Maps.newHashMap();

    private final Byte data;
    private final BlockFace capFace;

    private MushroomBlockTexture(final int data, /*@Nullable*/ final BlockFace capFace) {
        this.data = (byte) data;
        this.capFace = capFace;
    }

    /**
     * Gets the associated data value representing this mushroom block face.
     *
     * @return A byte containing the data value of this mushroom block face
     * @deprecated Magic value
     */
    @Deprecated
    public byte getData() {
        return data;
    }

    /**
     * Gets the face that has cap texture.
     *
     * @return The cap face
     */
    @Nullable
    public BlockFace getCapFace() {
        return capFace;
    }

    /**
     * Gets the MushroomBlockType with the given data value.
     *
     * @param data Data value to fetch
     * @return The {@link MushroomBlockTexture} representing the given value, or
     * null if it doesn't exist
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static MushroomBlockTexture getByData(final byte data) {
        return BY_DATA.get(data);
    }

    /**
     * Gets the MushroomBlockType with cap texture on the given block face.
     *
     * @param face the required block face with cap texture
     * @return The {@link MushroomBlockTexture} representing the given block
     * face, or null if it doesn't exist
     *
     * @see BlockFace
     */
    @Nullable
    public static MushroomBlockTexture getCapByFace(@Nullable final BlockFace face) {
        return BY_BLOCKFACE.get(face);
    }

    static {
        for (MushroomBlockTexture type : values()) {
            BY_DATA.put(type.data, type);
            BY_BLOCKFACE.put(type.capFace, type);
        }
    }
}

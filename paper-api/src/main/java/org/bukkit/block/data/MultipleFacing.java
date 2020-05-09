package org.bukkit.block.data;

import java.util.Set;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

/**
 * This class encompasses the 'north', 'east', 'south', 'west', 'up', 'down'
 * boolean flags which are used to set which faces of the block textures are
 * displayed on.
 * <br>
 * Some blocks may not be able to have faces on all directions, use
 * {@link #getAllowedFaces()} to get all possible faces for this block. It is
 * not valid to call any methods on non-allowed faces.
 */
public interface MultipleFacing extends BlockData {

    /**
     * Checks if this block has the specified face enabled.
     *
     * @param face to check
     * @return if face is enabled
     */
    boolean hasFace(@NotNull BlockFace face);

    /**
     * Set whether this block has the specified face enabled.
     *
     * @param face to set
     * @param has the face
     */
    void setFace(@NotNull BlockFace face, boolean has);

    /**
     * Get all of the faces which are enabled on this block.
     *
     * @return all faces enabled
     */
    @NotNull
    Set<BlockFace> getFaces();

    /**
     * Gets all of this faces which may be set on this block.
     *
     * @return all allowed faces
     */
    @NotNull
    Set<BlockFace> getAllowedFaces();
}

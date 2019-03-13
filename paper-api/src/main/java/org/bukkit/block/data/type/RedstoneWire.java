package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.AnaloguePowerable;
import org.jetbrains.annotations.NotNull;

/**
 * 'north', 'east', 'south', 'west' represent the types of connections this
 * redstone wire has to adjacent blocks.
 */
public interface RedstoneWire extends AnaloguePowerable {

    /**
     * Checks the type of connection on the specified face.
     *
     * @param face to check
     * @return connection type
     */
    @NotNull
    Connection getFace(@NotNull BlockFace face);

    /**
     * Sets the type of connection on the specified face.
     *
     * @param face to set
     * @param connection the connection type
     */
    void setFace(@NotNull BlockFace face, @NotNull Connection connection);

    /**
     * Gets all of this faces which may be set on this block.
     *
     * @return all allowed faces
     */
    @NotNull
    Set<BlockFace> getAllowedFaces();

    /**
     * The way in which a redstone wire can connect to an adjacent block face.
     */
    public enum Connection {
        /**
         * The wire travels up the side of the block adjacent to this face.
         */
        UP,
        /**
         * The wire travels flat from this face and into the adjacent block.
         */
        SIDE,
        /**
         * The wire does not connect in this direction.
         */
        NONE;
    }
}

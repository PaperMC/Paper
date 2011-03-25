package org.bukkit.block;

/**
 * Represents the face of a block
 */
public enum BlockFace {
    NORTH(-1, 0, 0),
    EAST(0, 0, -1),
    SOUTH(1, 0, 0),
    WEST(0, 0, 1),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    SELF(0, 0, 0);

    private final int modX;
    private final int modY;
    private final int modZ;

    private BlockFace(final int modX, final int modY, final int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }

    private BlockFace(final BlockFace face1, final BlockFace face2) {
        this.modX = face1.getModX() + face2.getModX();
        this.modY = face1.getModY() + face2.getModY();
        this.modZ = face1.getModZ() + face2.getModZ();
    }

    /**
     * Get the amount of X-coordinates to modify to get the represented block
     * @return Amount of X-coordinates to modify
     */
    public int getModX() {
        return modX;
    }

    /**
     * Get the amount of Y-coordinates to modify to get the represented block
     * @return Amount of Y-coordinates to modify
     */
    public int getModY() {
        return modY;
    }

    /**
     * Get the amount of Z-coordinates to modify to get the represented block
     * @return Amount of Z-coordinates to modify
     */
    public int getModZ() {
        return modZ;
    }
    
    public BlockFace getOppositeFace() {
        switch (this) {
        case NORTH:
            return BlockFace.SOUTH;
        case SOUTH:
            return BlockFace.NORTH;
        case EAST:
            return BlockFace.WEST;
        case WEST:
            return BlockFace.EAST;
        case UP:
            return BlockFace.DOWN;
        case DOWN:
            return BlockFace.UP;
        case NORTH_EAST:
            return BlockFace.SOUTH_WEST;
        case NORTH_WEST:
            return BlockFace.SOUTH_EAST;
        case SOUTH_EAST:
            return BlockFace.NORTH_WEST;
        case SOUTH_WEST:
            return BlockFace.NORTH_EAST;
        case SELF:
            return BlockFace.SELF;
        }
        
        return BlockFace.SELF;
    }
}

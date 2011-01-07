package org.bukkit;

/**
 * Represents the face of a block
 */
public enum BlockFace {
    North(-1, 0, 0),
    East(0, 0, -1),
    South(1, 0, 0),
    West(0, 0, 1),
    Up(0, 1, 0),
    Down(0, -1, 0),
    NorthEast(North, East),
    NorthWest(North, West),
    SouthEast(South, East),
    SouthWest(South, West),
    Self(0, 0, 0);

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
}

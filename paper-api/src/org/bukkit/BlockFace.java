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
    Down(0, -1, 0);

    private final int modX;
    private final int modY;
    private final int modZ;

    private BlockFace(final int modX, final int modY, final int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
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

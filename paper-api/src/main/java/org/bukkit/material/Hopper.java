package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a hopper in an active or deactivated state and facing in a
 * specific direction.
 *
 * @see Material#HOPPER
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Hopper extends MaterialData implements Directional, Redstone {

    protected static final BlockFace DEFAULT_DIRECTION = BlockFace.DOWN;
    protected static final boolean DEFAULT_ACTIVE = true;

    /**
     * Constructs a hopper facing the default direction (down) and initially
     * active.
     */
    public Hopper() {
        this(DEFAULT_DIRECTION, DEFAULT_ACTIVE);
    }

    /**
     * Constructs a hopper facing the specified direction and initially active.
     *
     * @param facingDirection the direction the hopper is facing
     *
     * @see BlockFace
     */
    public Hopper(BlockFace facingDirection) {
        this(facingDirection, DEFAULT_ACTIVE);
    }

    /**
     * Constructs a hopper facing the specified direction and either active or
     * not.
     *
     * @param facingDirection the direction the hopper is facing
     * @param isActive True if the hopper is initially active, false if
     * deactivated
     *
     * @see BlockFace
     */
    public Hopper(BlockFace facingDirection, boolean isActive) {
        super(Material.LEGACY_HOPPER);
        setFacingDirection(facingDirection);
        setActive(isActive);
    }

    public Hopper(Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Hopper(Material type, byte data) {
        super(type, data);
    }

    /**
     * Sets whether the hopper is active or not.
     *
     * @param isActive True if the hopper is active, false if deactivated as if
     * powered by redstone
     */
    public void setActive(boolean isActive) {
        setData((byte) (getData() & 0x7 | (isActive ? 0x0 : 0x8)));
    }

    /**
     * Checks whether the hopper is active or not.
     *
     * @return True if the hopper is active, false if deactivated
     */
    public boolean isActive() {
        return (getData() & 0x8) == 0;
    }

    /**
     * Sets the direction this hopper is facing
     *
     * @param face The direction to set this hopper to
     *
     * @see BlockFace
     */
    @Override
    public void setFacingDirection(BlockFace face) {
        int data = getData() & 0x8;

        switch (face) {
            case DOWN:
                data |= 0x0;
                break;
            case NORTH:
                data |= 0x2;
                break;
            case SOUTH:
                data |= 0x3;
                break;
            case WEST:
                data |= 0x4;
                break;
            case EAST:
                data |= 0x5;
                break;
        }

        setData((byte) data);
    }

    /**
     * Gets the direction this hopper is facing
     *
     * @return The direction this hopper is facing
     *
     * @see BlockFace
     */
    @Override
    public BlockFace getFacing() {
        byte data = (byte) (getData() & 0x7);

        switch (data) {
            default:
            case 0x0:
                return BlockFace.DOWN;
            case 0x2:
                return BlockFace.NORTH;
            case 0x3:
                return BlockFace.SOUTH;
            case 0x4:
                return BlockFace.WEST;
            case 0x5:
                return BlockFace.EAST;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing();
    }

    @Override
    public Hopper clone() {
        return (Hopper) super.clone();
    }

    /**
     * Checks if the hopper is powered.
     *
     * @return true if the hopper is powered
     */
    @Override
    public boolean isPowered() {
        return (getData() & 0x8) != 0;
    }
}

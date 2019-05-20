package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;

/**
 * Represents a door.
 *
 * This class was previously deprecated, but has been retrofitted to
 * work with modern doors. Some methods are undefined dependant on <code>isTopHalf()</code>
 * due to Minecraft's internal representation of doors.
 *
 * @see Material#LEGACY_WOODEN_DOOR
 * @see Material#LEGACY_IRON_DOOR_BLOCK
 * @see Material#LEGACY_SPRUCE_DOOR
 * @see Material#LEGACY_BIRCH_DOOR
 * @see Material#LEGACY_JUNGLE_DOOR
 * @see Material#LEGACY_ACACIA_DOOR
 * @see Material#LEGACY_DARK_OAK_DOOR
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Door extends MaterialData implements Directional, Openable {

    // This class breaks API contracts on Directional and Openable because
    // of the way doors are currently implemented. Beware!

    /**
     * @deprecated Artifact of old API, equivalent to new <code>Door(Material.LEGACY_WOODEN_DOOR);</code>
     */
    @Deprecated
    public Door() {
        super(Material.LEGACY_WOODEN_DOOR);
    }

    public Door(final Material type) {
        super(type);
    }

    /**
     * Constructs the bottom half of a door of the given material type, facing the specified direction and set to closed
     *
     * @param type The type of material this door is made of. This must match the type of the block above.
     * @param face The direction the door is facing.
     *
     * @see Material#LEGACY_WOODEN_DOOR
     * @see Material#LEGACY_IRON_DOOR_BLOCK
     * @see Material#LEGACY_SPRUCE_DOOR
     * @see Material#LEGACY_BIRCH_DOOR
     * @see Material#LEGACY_JUNGLE_DOOR
     * @see Material#LEGACY_ACACIA_DOOR
     * @see Material#LEGACY_DARK_OAK_DOOR
     *
     * @see BlockFace#WEST
     * @see BlockFace#NORTH
     * @see BlockFace#EAST
     * @see BlockFace#SOUTH
     */
    public Door(final Material type, BlockFace face) {
        this(type, face, false);
    }

    /**
     * Constructs the bottom half of a door of the given material type, facing the specified direction and set to open
     * or closed
     *
     * @param type The type of material this door is made of. This must match the type of the block above.
     * @param face The direction the door is facing.
     * @param isOpen Whether the door is currently opened.
     *
     * @see Material#LEGACY_WOODEN_DOOR
     * @see Material#LEGACY_IRON_DOOR_BLOCK
     * @see Material#LEGACY_SPRUCE_DOOR
     * @see Material#LEGACY_BIRCH_DOOR
     * @see Material#LEGACY_JUNGLE_DOOR
     * @see Material#LEGACY_ACACIA_DOOR
     * @see Material#LEGACY_DARK_OAK_DOOR
     *
     * @see BlockFace#WEST
     * @see BlockFace#NORTH
     * @see BlockFace#EAST
     * @see BlockFace#SOUTH
     */
    public Door(final Material type, BlockFace face, boolean isOpen) {
        super(type);
        setTopHalf(false);
        setFacingDirection(face);
        setOpen(isOpen);
    }

    /**
     * Constructs the top half of door of the given material type and with the hinge on the left or right
     *
     * @param type The type of material this door is made of. This must match the type of the block below.
     * @param isHingeRight True if the hinge is on the right hand side, false if the hinge is on the left hand side.
     *
     * @see Material#LEGACY_WOODEN_DOOR
     * @see Material#LEGACY_IRON_DOOR_BLOCK
     * @see Material#LEGACY_SPRUCE_DOOR
     * @see Material#LEGACY_BIRCH_DOOR
     * @see Material#LEGACY_JUNGLE_DOOR
     * @see Material#LEGACY_ACACIA_DOOR
     * @see Material#LEGACY_DARK_OAK_DOOR
     */
    public Door(final Material type, boolean isHingeRight) {
        super(type);
        setTopHalf(true);
        setHinge(isHingeRight);
    }

    /**
     * Constructs the bottom half of a wooden door of the given species, facing the specified direction and set to
     * closed
     *
     * @param species The species this wooden door is made of. This must match the species of the block above.
     * @param face The direction the door is facing.
     *
     * @see TreeSpecies
     *
     * @see BlockFace#WEST
     * @see BlockFace#NORTH
     * @see BlockFace#EAST
     * @see BlockFace#SOUTH
     */
    public Door(final TreeSpecies species, BlockFace face) {
        this(getWoodDoorOfSpecies(species), face, false);
    }

    /**
     * Constructs the bottom half of a wooden door of the given species, facing the specified direction and set to open
     * or closed
     *
     * @param species The species this wooden door is made of. This must match the species of the block above.
     * @param face The direction the door is facing.
     * @param isOpen Whether the door is currently opened.
     *
     * @see TreeSpecies
     *
     * @see BlockFace#WEST
     * @see BlockFace#NORTH
     * @see BlockFace#EAST
     * @see BlockFace#SOUTH
     */
    public Door(final TreeSpecies species, BlockFace face, boolean isOpen) {
        this(getWoodDoorOfSpecies(species), face, isOpen);
    }

    /**
     * Constructs the top half of a wooden door of the given species and with the hinge on the left or right
     *
     * @param species The species this wooden door is made of. This must match the species of the block below.
     * @param isHingeRight True if the hinge is on the right hand side, false if the hinge is on the left hand side.
     *
     * @see TreeSpecies
     */
    public Door(final TreeSpecies species, boolean isHingeRight) {
        this(getWoodDoorOfSpecies(species), isHingeRight);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Door(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Returns the item type of a wooden door for the given tree species.
     *
     * @param species The species of wood door required.
     * @return The item type for the given species.
     *
     * @see Material#LEGACY_WOODEN_DOOR
     * @see Material#LEGACY_SPRUCE_DOOR
     * @see Material#LEGACY_BIRCH_DOOR
     * @see Material#LEGACY_JUNGLE_DOOR
     * @see Material#LEGACY_ACACIA_DOOR
     * @see Material#LEGACY_DARK_OAK_DOOR
     */
    public static Material getWoodDoorOfSpecies(TreeSpecies species) {
        switch (species) {
            default:
            case GENERIC:
                return Material.LEGACY_WOODEN_DOOR;
            case BIRCH:
                return Material.LEGACY_BIRCH_DOOR;
            case REDWOOD:
                return Material.LEGACY_SPRUCE_DOOR;
            case JUNGLE:
                return Material.LEGACY_JUNGLE_DOOR;
            case ACACIA:
                return Material.LEGACY_ACACIA_DOOR;
            case DARK_OAK:
                return Material.LEGACY_DARK_OAK_DOOR;
        }
    }

    /**
     * Result is undefined if <code>isTopHalf()</code> is true.
     */
    @Override
    public boolean isOpen() {
        return ((getData() & 0x4) == 0x4);
    }

    /**
     * Set whether the door is open. Undefined if <code>isTopHalf()</code> is true.
     */
    @Override
    public void setOpen(boolean isOpen) {
        setData((byte) (isOpen ? (getData() | 0x4) : (getData() & ~0x4)));
    }

    /**
     * @return whether this is the top half of the door
     */
    public boolean isTopHalf() {
        return ((getData() & 0x8) == 0x8);
    }

    /**
     * Configure this part of the door to be either the top or the bottom half
     *
     * @param isTopHalf True to make it the top half.
     */
    public void setTopHalf(boolean isTopHalf) {
        setData((byte) (isTopHalf ? (getData() | 0x8) : (getData() & ~0x8)));
    }

    /**
     * @return BlockFace.SELF
     * @deprecated This method should not be used; use hinge and facing accessors instead.
     */
    @Deprecated
    public BlockFace getHingeCorner() {
        return BlockFace.SELF;
    }

    @Override
    public String toString() {
        return (isTopHalf() ? "TOP" : "BOTTOM") + " half of " + super.toString();
    }

    /**
     * Set the direction that this door should is facing.
     *
     * Undefined if <code>isTopHalf()</code> is true.
     *
     * @param face the direction
     */
    @Override
    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0xC);
        switch (face) {
            case WEST:
                data |= 0x0;
                break;
            case NORTH:
                data |= 0x1;
                break;
            case EAST:
                data |= 0x2;
                break;
            case SOUTH:
                data |= 0x3;
                break;
        }
        setData(data);
    }

    /**
     * Get the direction that this door is facing.
     *
     * Undefined if <code>isTopHalf()</code> is true.
     *
     * @return the direction
     */
    @Override
    public BlockFace getFacing() {
        byte data = (byte) (getData() & 0x3);
        switch (data) {
            case 0:
                return BlockFace.WEST;
            case 1:
                return BlockFace.NORTH;
            case 2:
                return BlockFace.EAST;
            case 3:
                return BlockFace.SOUTH;
            default:
                throw new IllegalStateException("Unknown door facing (data: " + data + ")");
        }
    }

    /**
     * Returns the side of the door the hinge is on.
     *
     * Undefined if <code>isTopHalf()</code> is false.
     *
     * @return false for left hinge, true for right hinge
     */
    public boolean getHinge() {
        return (getData() & 0x1) == 1;
    }

    /**
     * Set whether the hinge is on the left or right side. Left is false, right is true.
     *
     * Undefined if <code>isTopHalf()</code> is false.
     *
     * @param isHingeRight True if the hinge is on the right hand side, false if the hinge is on the left hand side.
     */
    public void setHinge(boolean isHingeRight) {
        setData((byte) (isHingeRight ? (getData() | 0x1) : (getData() & ~0x1)));
    }

    @Override
    public Door clone() {
        return (Door) super.clone();
    }
}

package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a trap door
 */
public class TrapDoor extends SimpleAttachableMaterialData implements Openable {
    public TrapDoor() {
        super(Material.TRAP_DOOR);
    }

    public TrapDoor(final int type) {
        super(type);
    }

    public TrapDoor(final Material type) {
        super(type);
    }

    public TrapDoor(final int type, final byte data) {
        super(type, data);
    }

    public TrapDoor(final Material type, final byte data) {
        super(type, data);
    }

    public boolean isOpen() {
        return ((getData() & 0x4) == 0x4);
    }

    public void setOpen(boolean isOpen) {
        byte data = getData();

        if (isOpen) {
            data |= 0x4;
        } else {
            data &= ~0x4;
        }

        setData(data);
    }

    public BlockFace getAttachedFace() {
        byte data = (byte) (getData() & 0x3);

        switch (data) {
            case 0x0:
                return BlockFace.WEST;

            case 0x1:
                return BlockFace.EAST;

            case 0x2:
                return BlockFace.SOUTH;

            case 0x3:
                return BlockFace.NORTH;
        }

        return null;

    }

    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() & 0x4);

        switch (face) {
            case WEST:
                data |= 0x1;
                break;
            case NORTH:
                data |= 0x2;
                break;
            case SOUTH:
                data |= 0x3;
                break;
        }

        setData(data);
    }

    @Override
    public String toString() {
        return (isOpen() ? "OPEN " : "CLOSED ") + super.toString() + " with hinges set " + getAttachedFace();
    }

    @Override
    public TrapDoor clone() {
        return (TrapDoor) super.clone();
    }
}
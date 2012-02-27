package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a fence gate
 */
public class Gate extends MaterialData implements Directional, Openable {
    private static final byte OPEN_BIT = 0x4;
    private static final byte DIR_BIT = 0x3;
    private static final byte GATE_SOUTH = 0x0;
    private static final byte GATE_WEST = 0x1;
    private static final byte GATE_NORTH = 0x2;
    private static final byte GATE_EAST = 0x3;

    public Gate() {
        super(Material.FENCE_GATE);
    }

    public Gate(int type, byte data){
        super(type, data);
    }

    public Gate(byte data) {
        super(Material.FENCE_GATE, data);
    }

    public void setFacingDirection(BlockFace face) {
        byte data = (byte) (getData() &~ DIR_BIT);

        switch (face) {
            default:
            case SOUTH:
                data |= GATE_SOUTH;
                break;
            case WEST:
                data |= GATE_WEST;
                break;
            case NORTH:
                data |= GATE_NORTH;
                break;
            case EAST:
                data |= GATE_EAST;
                break;
        }

        setData(data);
    }

    public BlockFace getFacing() {
        switch (getData() & DIR_BIT) {
            case GATE_SOUTH:
                return BlockFace.SOUTH;
            case GATE_WEST:
                return BlockFace.WEST;
            case GATE_NORTH:
                return BlockFace.NORTH;
            case GATE_EAST:
                return BlockFace.EAST;
        }

        return BlockFace.SOUTH;
    }

    public boolean isOpen() {
        return (getData() & OPEN_BIT) > 0;
    }

    public void setOpen(boolean isOpen) {
        byte data = getData();

        if (isOpen) {
            data |= OPEN_BIT;
        } else {
            data &= ~OPEN_BIT;
        }

        setData(data);
    }

    @Override
    public String toString() {
        return (isOpen() ? "OPEN " : "CLOSED ") + " facing and opening " + getFacing();
    }

    @Override
    public Gate clone() {
        return (Gate) super.clone();
    }
}
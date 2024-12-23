package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents the tripwire hook
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated(since = "1.13", forRemoval = true)
public class TripwireHook extends SimpleAttachableMaterialData implements Redstone {

    public TripwireHook() {
        super(Material.LEGACY_TRIPWIRE_HOOK);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public TripwireHook(final Material type, final byte data) {
        super(type, data);
    }

    public TripwireHook(BlockFace dir) {
        this();
        setFacingDirection(dir);
    }

    /**
     * Test if tripwire is connected
     *
     * @return true if connected, false if not
     */
    public boolean isConnected() {
        return (getData() & 0x4) != 0;
    }

    /**
     * Set tripwire connection state
     *
     * @param connected - true if connected, false if not
     */
    public void setConnected(boolean connected) {
        int dat = getData() & (0x8 | 0x3);
        if (connected) {
            dat |= 0x4;
        }
        setData((byte) dat);
    }

    /**
     * Test if hook is currently activated
     *
     * @return true if activated, false if not
     */
    public boolean isActivated() {
        return (getData() & 0x8) != 0;
    }

    /**
     * Set hook activated state
     *
     * @param act - true if activated, false if not
     */
    public void setActivated(boolean act) {
        int dat = getData() & (0x4 | 0x3);
        if (act) {
            dat |= 0x8;
        }
        setData((byte) dat);
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        int dat = getData() & 0xC;
        switch (face) {
        case WEST:
            dat |= 0x1;
            break;
        case NORTH:
            dat |= 0x2;
            break;
        case EAST:
            dat |= 0x3;
            break;
        case SOUTH:
        default:
            break;
        }
        setData((byte) dat);
    }

    @Override
    public BlockFace getAttachedFace() {
        switch (getData() & 0x3) {
        case 0:
            return BlockFace.NORTH;
        case 1:
            return BlockFace.EAST;
        case 2:
            return BlockFace.SOUTH;
        case 3:
            return BlockFace.WEST;
        }
        return null;
    }

    @Override
    public boolean isPowered() {
        return isActivated();
    }

    @Override
    public TripwireHook clone() {
        return (TripwireHook) super.clone();
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing() + (isActivated() ? " Activated" : "") + (isConnected() ? " Connected" : "");
    }
}

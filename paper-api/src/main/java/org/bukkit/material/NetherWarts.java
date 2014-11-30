package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;

/**
 * Represents nether wart
 */
public class NetherWarts extends MaterialData {
    public NetherWarts() {
        super(Material.NETHER_WARTS);
    }

    public NetherWarts(NetherWartsState state) {
        this();
        setState(state);
    }

    /**
     * @param type the raw type id
     * @deprecated Magic value
     */
    @Deprecated
    public NetherWarts(final int type) {
        super(type);
    }

    public NetherWarts(final Material type) {
        super (type);
    }

    /**
     * @param type the raw type id
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public NetherWarts(final int type, final byte data) {
        super(type, data);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public NetherWarts(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current growth state of this nether wart
     *
     * @return NetherWartsState of this nether wart
     */
    public NetherWartsState getState() {
        switch (getData()) {
            case 0:
                return NetherWartsState.SEEDED;
            case 1:
                return NetherWartsState.STAGE_ONE;
            case 2:
                return NetherWartsState.STAGE_TWO;
            default:
                return NetherWartsState.RIPE;
        }
    }

    /**
     * Sets the growth state of this nether wart
     *
     * @param state New growth state of this nether wart
     */
    public void setState(NetherWartsState state) {
        switch (state) {
            case SEEDED:
                setData((byte) 0x0);
                return;
            case STAGE_ONE:
                setData((byte) 0x1);
                return;
            case STAGE_TWO:
                setData((byte) 0x2);
                return;
            case RIPE:
                setData((byte) 0x3);
                return;
        }
    }

    @Override
    public String toString() {
        return getState() + " " + super.toString();
    }

    @Override
    public NetherWarts clone() {
        return (NetherWarts) super.clone();
    }
}

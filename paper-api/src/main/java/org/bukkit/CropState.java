package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different growth states of crops
 * @author sunkid
 */
public enum CropState {
    /**
     * State when first seeded
     */
    SEEDED((byte) 0x0),
    /**
     * First growth stage
     */
    GERMINATED((byte) 0x1),
    /**
     * Second growth stage
     */
    VERY_SMALL((byte) 0x2),
    /**
     * Third growth stage
     */
    SMALL((byte) 0x3),
    /**
     * Fourth growth stage
     */
    MEDIUM((byte) 0x4),
    /**
     * Fifth growth stage
     */
    TALL((byte) 0x5),
    /**
     * Almost ripe stage
     */
    VERY_TALL((byte) 0x6),
    /**
     * Ripe stage
     */
    RIPE((byte) 0x7);

    private final byte data;
    private final static Map<Byte, CropState> states = new HashMap<Byte, CropState>();

    private CropState(final byte data) {
        this.data = data;
    }

    /**
     * Gets the associated data value representing this growth state
     *
     * @return A byte containing the data value of this growth state
     */
    public byte getData() {
        return data;
    }

    /**
     * Gets the CropState with the given data value
     *
     * @param data
     *            Data value to fetch
     * @return The {@link CropState} representing the given value, or null if
     *         it doesn't exist
     */
    public static CropState getByData(final byte data) {
        return states.get(data);
    }

    static {
        for (CropState s : CropState.values()) {
            states.put(s.getData(), s);
        }
    }
}

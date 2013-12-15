package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Represents the different growth states of crops
 */
public enum CropState {

    /**
     * State when first seeded
     */
    SEEDED(0x0),
    /**
     * First growth stage
     */
    GERMINATED(0x1),
    /**
     * Second growth stage
     */
    VERY_SMALL(0x2),
    /**
     * Third growth stage
     */
    SMALL(0x3),
    /**
     * Fourth growth stage
     */
    MEDIUM(0x4),
    /**
     * Fifth growth stage
     */
    TALL(0x5),
    /**
     * Almost ripe stage
     */
    VERY_TALL(0x6),
    /**
     * Ripe stage
     */
    RIPE(0x7);

    private final byte data;
    private final static Map<Byte, CropState> BY_DATA = Maps.newHashMap();

    private CropState(final int data) {
        this.data = (byte) data;
    }

    /**
     * Gets the associated data value representing this growth state
     *
     * @return A byte containing the data value of this growth state
     * @deprecated Magic value
     */
    @Deprecated
    public byte getData() {
        return data;
    }

    /**
     * Gets the CropState with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link CropState} representing the given value, or null if
     *     it doesn't exist
     * @deprecated Magic value
     */
    @Deprecated
    public static CropState getByData(final byte data) {
        return BY_DATA.get(data);
    }

    static {
        for (CropState cropState : values()) {
            BY_DATA.put(cropState.getData(), cropState);
        }
    }
}

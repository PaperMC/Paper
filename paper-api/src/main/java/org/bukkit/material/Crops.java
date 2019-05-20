package org.bukkit.material;

import org.bukkit.CropState;
import org.bukkit.Material;

/**
 * Represents the different types of crops in different states of growth.
 *
 * @see Material#LEGACY_CROPS
 * @see Material#LEGACY_CARROT
 * @see Material#LEGACY_POTATO
 * @see Material#LEGACY_BEETROOT_BLOCK
 * @see Material#LEGACY_NETHER_WARTS
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Crops extends MaterialData {
    protected static final Material DEFAULT_TYPE = Material.LEGACY_CROPS;
    protected static final CropState DEFAULT_STATE = CropState.SEEDED;

    /**
     * Constructs a wheat crop block in the seeded state.
     */
    public Crops() {
        this(DEFAULT_TYPE, DEFAULT_STATE);
    }

    /**
     * Constructs a wheat crop block in the given growth state
     *
     * @param state The growth state of the crops
     */
    public Crops(CropState state) {
        this(DEFAULT_TYPE, state);
        setState(state);
    }

    /**
     * Constructs a crop block of the given type and in the given growth state
     *
     * @param type The type of crops
     * @param state The growth state of the crops
     */
    public Crops(final Material type, final CropState state) {
        super(type);
        setState(state);
    }

    /**
     * Constructs a crop block of the given type and in the seeded state
     *
     * @param type The type of crops
     */
    public Crops(final Material type) {
        this(type, DEFAULT_STATE);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Crops(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current growth state of this crop
     *
     * For crops with only four growth states such as beetroot, only the values SEEDED, SMALL, TALL and RIPE will be
     * returned.
     *
     * @return CropState of this crop
     */
    public CropState getState() {
        switch (getItemType()) {
            case LEGACY_CROPS:
            case LEGACY_CARROT:
            case LEGACY_POTATO:
                // Mask the data just in case top bit set
                return CropState.getByData((byte) (getData() & 0x7));
            case LEGACY_BEETROOT_BLOCK:
            case LEGACY_NETHER_WARTS:
                // Mask the data just in case top bits are set
                // Will return SEEDED, SMALL, TALL, RIPE for the three growth data values
                return CropState.getByData((byte) (((getData() & 0x3) * 7 + 2) / 3));
            default:
                throw new IllegalArgumentException("Block type is not a crop");
        }
    }

    /**
     * Sets the growth state of this crop
     *
     * For crops with only four growth states such as beetroot, the 8 CropStates are mapped into four states:
     *
     * SEEDED, SMALL, TALL and RIPE
     *
     * GERMINATED will change to SEEDED
     * VERY_SMALL will change to SMALL
     * MEDIUM will change to TALL
     * VERY_TALL will change to RIPE
     *
     * @param state New growth state of this crop
     */
    public void setState(CropState state) {
        switch (getItemType()) {
            case LEGACY_CROPS:
            case LEGACY_CARROT:
            case LEGACY_POTATO:
                // Preserve the top bit in case it is set
                setData((byte) ((getData() & 0x8) | state.getData()));
                break;
            case LEGACY_NETHER_WARTS:
            case LEGACY_BEETROOT_BLOCK:
                // Preserve the top bits in case they are set
                setData((byte) ((getData() & 0xC) | (state.getData() >> 1)));
                break;
            default:
                throw new IllegalArgumentException("Block type is not a crop");
        }
    }

    @Override
    public String toString() {
        return getState() + " " + super.toString();
    }

    @Override
    public Crops clone() {
        return (Crops) super.clone();
    }
}

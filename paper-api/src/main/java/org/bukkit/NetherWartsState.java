package org.bukkit;

// Paper start
/**
 * @deprecated use {@link org.bukkit.block.data.BlockData} and {@link org.bukkit.block.data.Ageable}
 */
@Deprecated(forRemoval = true, since = "1.13")
// Paper end
public enum NetherWartsState {

    /**
     * State when first seeded
     */
    SEEDED,
    /**
     * First growth stage
     */
    STAGE_ONE,
    /**
     * Second growth stage
     */
    STAGE_TWO,
    /**
     * Ready to harvest
     */
    RIPE;
}

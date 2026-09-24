package org.bukkit.block;

/**
 * Represents a captured state of a potent sulfur block.
 * <p>
 * A potent sulfur block only acts as a geyser while it has a valid water column above it:
 * one to four water source blocks directly above it (waterlogged blocks without collision
 * count as water), topped by air or another block without collision. This is a vanilla
 * limitation that also applies on the client, so it cannot be bypassed by any method in
 * this class. Without a water column, a geyser does not count down, erupt, launch entities
 * or apply nausea, regardless of its {@link #getEruptionMode() eruption mode}.
 */
public interface PotentSulfur extends TileState {

    /**
     * Gets the eruption mode of this geyser.
     *
     * @return the eruption mode
     */
    EruptionMode getEruptionMode();

    /**
     * Sets the eruption mode of this geyser, overriding the behavior normally
     * determined by the block below it.
     * <p>
     * The mode is persistent and applied to the block in the world once this
     * state is {@link #update() updated}.
     * <p>
     * The mode only has an effect while the geyser has a valid water column
     * above it, see {@link PotentSulfur}. Without a water source block directly
     * above it, the block stays {@link org.bukkit.block.data.type.PotentSulfur.State#DRY dry}
     * and the mode is applied once water is added.
     *
     * @param mode the eruption mode
     */
    void setEruptionMode(EruptionMode mode);

    /**
     * Makes this geyser erupt once, regardless of the block below it and its
     * {@link #getEruptionMode() eruption mode}. Once the eruption ends, the geyser
     * returns to the behavior of its eruption mode.
     * <p>
     * Unlike most methods on this state, this acts on the block in the world
     * immediately. If the eruption is started, this captured state no longer
     * matches the block in the world, and {@link #update() updating} it would undo the eruption.
     * <p>
     * This fails if the geyser is dry, already erupting, or does not have a
     * valid water column above it, see {@link PotentSulfur}.
     *
     * @return {@code true} if an eruption was started, in which case this state is now outdated
     * @throws IllegalStateException if this block state is not placed
     */
    boolean erupt();

    /**
     * Controls when a potent sulfur geyser erupts.
     * <p>
     * Each mode is reflected in the block's
     * {@link org.bukkit.block.data.type.PotentSulfur#getPotentSulfurState() state}.
     */
    enum EruptionMode {
        /**
         * Eruptions are determined by the block below, like in vanilla: magma blocks
         * cause periodic eruptions and lava causes continuous eruptions.
         */
        DEFAULT,
        /**
         * The geyser never erupts on its own. The block will be
         * {@link org.bukkit.block.data.type.PotentSulfur.State#WET wet}.
         */
        NEVER,
        /**
         * The geyser periodically erupts, as if there was a magma block below it.
         */
        PERIODIC,
        /**
         * The geyser continuously erupts, as if there was lava below it.
         */
        CONTINUOUS
    }
}

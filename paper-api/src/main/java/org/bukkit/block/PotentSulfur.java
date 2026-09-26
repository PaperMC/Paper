package org.bukkit.block;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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
@NullMarked
public interface PotentSulfur extends TileState {

    /**
     * Gets the eruption mode this geyser follows.
     * <p>
     * This is the {@link #getEruptionModeOverride() eruption mode override} if one is set.
     * Otherwise, it is determined by the block below the geyser in the world, like in
     * vanilla: a magma block causes {@link EruptionMode#PERIODIC periodic} eruptions, lava
     * causes {@link EruptionMode#CONTINUOUS continuous} eruptions, and any other block
     * causes {@link EruptionMode#NEVER no} eruptions.
     * <p>
     * The eruption mode describes how the geyser behaves while it has a valid water column
     * above it, see {@link PotentSulfur}. Use the block's
     * {@link org.bukkit.block.data.type.PotentSulfur#getPotentSulfurState() state} to see
     * what the geyser is doing right now.
     *
     * @return the eruption mode
     * @throws IllegalStateException if no override is set and this block state is not placed
     */
    EruptionMode getEruptionMode();

    /**
     * Gets the eruption mode override of this geyser.
     *
     * @return the eruption mode override, or {@code null} if the eruption mode is
     *     determined by the block below, like in vanilla
     * @see #getEruptionMode()
     */
    @Nullable EruptionMode getEruptionModeOverride();

    /**
     * Sets the eruption mode override of this geyser, replacing the behavior normally
     * determined by the block below it.
     * <p>
     * The override is persistent and applied to the block in the world once this
     * state is {@link #update() updated}.
     * <p>
     * The override only has an effect while the geyser has a valid water column
     * above it, see {@link PotentSulfur}. Without a water source block directly
     * above it, the block stays {@link org.bukkit.block.data.type.PotentSulfur.State#DRY dry}
     * and the override is applied once water is added.
     *
     * @param mode the eruption mode override, or {@code null} to let the block below
     *     determine the eruption mode, like in vanilla
     */
    void setEruptionModeOverride(@Nullable EruptionMode mode);

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
         * The geyser never erupts on its own. The block will be
         * {@link org.bukkit.block.data.type.PotentSulfur.State#WET wet}.
         * <p>
         * In vanilla, this is the case for any block below the geyser other than magma blocks and lava.
         */
        NEVER,
        /**
         * The geyser periodically erupts, alternating between
         * {@link org.bukkit.block.data.type.PotentSulfur.State#DORMANT dormant} and
         * {@link org.bukkit.block.data.type.PotentSulfur.State#ERUPTING erupting}.
         * <p>
         * In vanilla, this is caused by a magma block below the geyser.
         */
        PERIODIC,
        /**
         * The geyser continuously erupts. The block will be
         * {@link org.bukkit.block.data.type.PotentSulfur.State#CONTINUOUS continuous}.
         * <p>
         * In vanilla, this is caused by lava below the geyser.
         */
        CONTINUOUS
    }
}

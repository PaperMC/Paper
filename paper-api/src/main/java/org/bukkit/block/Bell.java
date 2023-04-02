package org.bukkit.block;

import org.bukkit.entity.Entity;
import org.bukkit.event.block.BellRingEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of Bell.
 */
public interface Bell extends TileState {

    /**
     * Ring this bell. This will call a {@link BellRingEvent}.
     *
     * @param entity the entity ringing the bell
     * @param direction the direction from which the bell was rung or null to
     * ring in the direction that the bell is facing
     * @return true if rung successfully, false if the event was cancelled
     */
    public boolean ring(@Nullable Entity entity, @Nullable BlockFace direction);

    /**
     * Ring this bell in the direction that the bell is facing. This will call a
     * {@link BellRingEvent}.
     *
     * @param entity the entity ringing the bell
     * @return true if rung successfully, false if the event was cancelled
     */
    public boolean ring(@Nullable Entity entity);

    /**
     * Ring this bell. This will call a {@link BellRingEvent}.
     *
     * @param direction the direction from which the bell was rung or null to
     * ring in the direction that the bell is facing
     * @return true if rung successfully, false if the event was cancelled
     */
    public boolean ring(@Nullable BlockFace direction);

    /**
     * Ring this bell in the direction that the bell is facing. This will call a
     * {@link BellRingEvent}.
     *
     * @return true if rung successfully, false if the event was cancelled
     */
    public boolean ring();

    /**
     * Check whether or not this bell is shaking. A bell is considered to be
     * shaking if it was recently rung.
     * <p>
     * A bell will typically shake for 50 ticks.
     *
     * @return true if shaking, false otherwise
     */
    public boolean isShaking();

    /**
     * Get the amount of ticks since this bell has been shaking, or 0 if the
     * bell is not currently shaking.
     * <p>
     * A bell will typically shake for 50 ticks.
     *
     * @return the time in ticks since the bell was rung, or 0 if not shaking
     */
    public int getShakingTicks();

    /**
     * Check whether or not this bell is resonating. A bell is considered to be
     * resonating if {@link #isShaking() while shaking}, raiders were detected
     * in the area and are ready to be highlighted to nearby players.
     * <p>
     * A bell will typically resonate for 40 ticks.
     *
     * @return true if resonating, false otherwise
     */
    public boolean isResonating();

    /**
     * Get the amount of ticks since this bell has been resonating, or 0 if the
     * bell is not currently resonating.
     * <p>
     * A bell will typically resonate for 40 ticks.
     *
     * @return the time in ticks since the bell has been resonating, or 0 if not
     * resonating
     */
    public int getResonatingTicks();
}

package org.bukkit.entity;

import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 1.14
 */
public interface Raider extends Monster {

    /**
     * Set the {@link Raid} that this raider is participating in.
     *
     * @param raid the raid to set
     * @since 1.20.1
     */
    void setRaid(@Nullable Raid raid);

    /**
     * Get the {@link Raid} that this raider is participating in, if any.
     *
     * @return the raid, or null if not participating in a raid
     * @since 1.20.1
     */
    @Nullable
    Raid getRaid();

    /**
     * Get the raid wave that this raider spawned as part of.
     *
     * @return the raid wave, or 0 if not participating in a raid
     * @since 1.20.1
     */
    int getWave();

    /**
     * Set the raid wave that this raider was spawned as part of.
     *
     * @param wave the raid wave to set. Must be >= 0
     * @since 1.20.1
     */
    void setWave(int wave);

    /**
     * Gets the block the raider is targeting to patrol.
     *
     * @return target block or null
     */
    @Nullable
    Block getPatrolTarget();

    /**
     * Sets the block the raider is targeting to patrol.
     *
     * @param block target block or null. Must be in same world as the entity
     */
    void setPatrolTarget(@Nullable Block block);

    /**
     * Gets whether this entity is a patrol leader.
     *
     * @return patrol leader status
     */
    boolean isPatrolLeader();

    /**
     * Sets whether this entity is a patrol leader.
     *
     * @param leader patrol leader status
     */
    void setPatrolLeader(boolean leader);

    /**
     * Gets whether this mob can join an active raid.
     *
     * @return CanJoinRaid status
     * @since 1.15.1
     */
    boolean isCanJoinRaid();

    /**
     * Sets whether this mob can join an active raid.
     *
     * @param join CanJoinRaid status
     * @since 1.15.1
     */
    void setCanJoinRaid(boolean join);

    /**
     * Get the amount of ticks that this mob has exited the bounds of a village
     * as a raid participant.
     * <p>
     * This value is increased only when the mob has had no action for 2,400 ticks
     * (according to {@link #getNoActionTicks()}). Once both the no action ticks have
     * reached that value and the ticks outside a raid exceeds 30, the mob will be
     * expelled from the raid.
     *
     * @return the ticks outside of a raid
     * @since 1.20.1
     */
    int getTicksOutsideRaid();

    /**
     * Set the amount of ticks that this mob has exited the bounds of a village
     * as a raid participant.
     * <p>
     * This value is considered only when the mob has had no action for 2,400 ticks
     * (according to {@link #getNoActionTicks()}). Once both the no action ticks have
     * reached that value and the ticks outside a raid exceeds 30, the mob will be
     * expelled from the raid.
     *
     * @param ticks the ticks outside of a raid
     * @since 1.20.1
     */
    void setTicksOutsideRaid(int ticks);

    /**
     * Check whether or not this raider is celebrating a raid victory.
     *
     * @return true if celebrating, false otherwise
     * @since 1.18.2
     */
    boolean isCelebrating();

    /**
     * Set whether or not this mob is celebrating a raid victory.
     *
     * @param celebrating whether or not to celebrate
     * @since 1.18.2
     */
    void setCelebrating(boolean celebrating);

    /**
     * Get the {@link Sound} this entity will play when celebrating.
     *
     * @return the celebration sound
     * @since 1.19.2
     */
    @NotNull
    Sound getCelebrationSound();
}

package org.bukkit.entity;

import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public interface Raider extends Monster {

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
     */
    boolean isCanJoinRaid();

    /**
     * Sets whether this mob can join an active raid.
     *
     * @param join CanJoinRaid status
     */
    void setCanJoinRaid(boolean join);
}

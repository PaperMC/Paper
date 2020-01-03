package com.destroystokyo.paper.entity.ai;

import java.util.EnumSet;
import org.bukkit.entity.Mob;
import org.jspecify.annotations.NullMarked;

/**
 * Represents an AI goal of an entity
 */
@NullMarked
public interface Goal<T extends Mob> {

    /**
     * Checks if this goal should be activated
     *
     * @return if this goal should be activated
     */
    boolean shouldActivate();

    /**
     * Checks if this goal should stay active, defaults to {@link Goal#shouldActivate()}
     *
     * @return if this goal should stay active
     */
    default boolean shouldStayActive() {
        return this.shouldActivate();
    }

    /**
     * Called when this goal gets activated
     */
    default void start() {
    }

    /**
     * Called when this goal gets stopped
     */
    default void stop() {
    }

    /**
     * Called each tick the goal is activated
     */
    default void tick() {
    }

    /**
     * A unique key that identifies this type of goal. Plugins should use their own namespace, not the minecraft
     * namespace. Additionally, this key also specifies to what mobs this goal can be applied to
     *
     * @return the goal key
     */
    GoalKey<T> getKey();

    /**
     * Returns a list of all applicable flags for this goal.<br>
     * <p>
     * This method is only called on construction.
     *
     * @return the subtypes.
     */
    EnumSet<GoalType> getTypes();
}

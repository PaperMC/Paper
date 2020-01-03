package com.destroystokyo.paper.entity.ai;

/**
 * Represents the subtype of a goal. Used by minecraft to disable certain types of goals if needed.
 */
public enum GoalType {

    MOVE,
    LOOK,
    JUMP,
    TARGET,
    /**
     * Used to map vanilla goals, that are a behavior goal but don't have a type set...
     */
    UNKNOWN_BEHAVIOR,

}

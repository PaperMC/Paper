package org.bukkit.entity;

/**
 * Represents an entity body pose.
 */
public enum Pose {

    /**
     * Entity is standing normally.
     *
     */
    STANDING,
    /**
     * Entity is gliding.
     */
    FALL_FLYING,
    /**
     * Entity is sleeping.
     */
    SLEEPING,
    /**
     * Entity is swimming.
     */
    SWIMMING,
    /**
     * Entity is riptiding with a trident.
     */
    SPIN_ATTACK,
    /**
     * Entity is sneaking.
     */
    SNEAKING,
    /**
     * Entity is dead.
     */
    DYING;
}

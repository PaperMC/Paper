package org.bukkit;

import org.bukkit.entity.HumanEntity;

/**
 * Represents the various type of game modes that {@link HumanEntity}s may have
 */
public enum GameMode {
    /**
     * Creative mode may fly, build instantly, become invulnerable and create free items
     */
    CREATIVE,
    
    /**
     * Survival mode is the "normal" gameplay type, with no special features.
     */
    SURVIVAL;
}

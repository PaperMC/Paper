package org.bukkit;

/**
 * Represents various types of portals that can be made in a world.
 */
public enum PortalType {

    /**
     * This is a Nether portal, made of obsidian.
     */
    NETHER,

    /**
     * This is an Ender portal.
     */
    ENDER,
    // Paper start
    /**
     * This is an end gateway
     */
    END_GATEWAY,
    // Paper end

    /**
     * This is a custom Plugin portal.
     */
    CUSTOM;
}

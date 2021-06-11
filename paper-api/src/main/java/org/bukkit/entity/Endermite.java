package org.bukkit.entity;

public interface Endermite extends Monster {

    /**
     * Gets whether this Endermite was spawned by a player.
     *
     * An Endermite spawned by a player will be attacked by nearby Enderman.
     *
     * @return player spawned status
     * @deprecated this functionality no longer exists
     */
    @Deprecated
    boolean isPlayerSpawned();

    /**
     * Sets whether this Endermite was spawned by a player.
     *
     * An Endermite spawned by a player will be attacked by nearby Enderman.
     *
     * @param playerSpawned player spawned status
     * @deprecated this functionality no longer exists
     */
    @Deprecated
    void setPlayerSpawned(boolean playerSpawned);
}

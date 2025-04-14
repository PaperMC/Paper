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
    @Deprecated(since = "1.17", forRemoval = true)
    boolean isPlayerSpawned();

    /**
     * Sets whether this Endermite was spawned by a player.
     *
     * An Endermite spawned by a player will be attacked by nearby Enderman.
     *
     * @param playerSpawned player spawned status
     * @deprecated this functionality no longer exists
     */
    @Deprecated(since = "1.17", forRemoval = true)
    void setPlayerSpawned(boolean playerSpawned);
    // Paper start
    /**
     * Sets how many ticks this endermite has been living for.
     * If this value is greater than 2400, this endermite will despawn.
     *
     * @param ticks lifetime ticks
     */
    void setLifetimeTicks(int ticks);

    /**
     * Gets how long this endermite has been living for.
     * This value will tick up while {@link LivingEntity#getRemoveWhenFarAway()} is false.
     * If this value is greater than 2400, this endermite will despawn.
     *
     * @return lifetime ticks
     */
    int getLifetimeTicks();
    // Paper end
}

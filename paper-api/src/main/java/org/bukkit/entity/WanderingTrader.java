package org.bukkit.entity;

/**
 * Represents a wandering trader NPC
 */
public interface WanderingTrader extends AbstractVillager {

    /**
     * Gets the despawn delay before this {@link WanderingTrader} is forcibly
     * despawned.
     *
     * If this is less than or equal to 0, then the trader will not be
     * despawned.
     *
     * @return The despawn delay before this {@link WanderingTrader} is forcibly
     * despawned
     */
    public int getDespawnDelay();

    /**
     * Sets the despawn delay before this {@link WanderingTrader} is forcibly
     * despawned.
     *
     * If this is less than or equal to 0, then the trader will not be
     * despawned.
     *
     * @param despawnDelay The new despawn delay before this
     * {@link WanderingTrader} is forcibly despawned
     */
    public void setDespawnDelay(int despawnDelay);
}

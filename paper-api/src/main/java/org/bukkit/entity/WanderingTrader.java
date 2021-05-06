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
    
    // Paper start - Add more WanderingTrader API
    /**
     * Set if the Wandering Trader can and will drink an invisibility potion.
     * @param bool whether the mob will drink
     */
    public void setCanDrinkPotion(boolean bool);

    /**
     * Get if the Wandering Trader can and will drink an invisibility potion.
     * @return whether the mob will drink
     */
    public boolean canDrinkPotion();

    /**
     * Set if the Wandering Trader can and will drink milk.
      * @param bool whether the mob will drink
     */
    public void setCanDrinkMilk(boolean bool);

    /**
     * Get if the Wandering Trader can and will drink milk.
     * @return whether the mob will drink
     */
    public boolean canDrinkMilk();
    // Paper end
}

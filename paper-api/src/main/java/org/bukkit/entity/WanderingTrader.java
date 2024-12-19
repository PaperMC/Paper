package org.bukkit.entity;

/**
 * Represents a wandering trader NPC
 *
 * @since 1.14
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
     * @since 1.16.5
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
     * @since 1.16.5
     */
    public void setDespawnDelay(int despawnDelay);
    
    // Paper start - Add more WanderingTrader API
    /**
     * Set if the Wandering Trader can and will drink an invisibility potion.
     * @param bool whether the mob will drink
     * @since 1.16.5
     */
    public void setCanDrinkPotion(boolean bool);

    /**
     * Get if the Wandering Trader can and will drink an invisibility potion.
     * @return whether the mob will drink
     * @since 1.16.5
     */
    public boolean canDrinkPotion();

    /**
     * Set if the Wandering Trader can and will drink milk.
      * @param bool whether the mob will drink
     * @since 1.16.5
     */
    public void setCanDrinkMilk(boolean bool);

    /**
     * Get if the Wandering Trader can and will drink milk.
     * @return whether the mob will drink
     * @since 1.16.5
     */
    public boolean canDrinkMilk();

    /**
     * Gets the location that this wandering trader is currently
     * wandering towards.
     * <p>
     * This will return null if the wandering trader has finished
     * wandering towards the given location.
     *
     * @return the location currently wandering towards, or null if not wandering
     * @since 1.19.2
     */
    @org.jetbrains.annotations.Nullable
    org.bukkit.Location getWanderingTowards();

    /**
     * Sets the location that this wandering trader is currently wandering towards.
     * <p>
     * This can be set to null to prevent the wandering trader from wandering further.
     *
     * @param location location to wander towards (world is ignored, will always use the entity's world)
     * @since 1.19.2
     */
    void setWanderingTowards(@org.jetbrains.annotations.Nullable org.bukkit.Location location);
    // Paper end
}

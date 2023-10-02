package org.bukkit.entity;

import org.bukkit.GameEvent;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an instance of a lightning strike. May or may not do damage.
 */
public interface LightningStrike extends Entity {

    /**
     * Returns whether the strike is an effect that does no damage.
     *
     * @return whether the strike is an effect
     */
    public boolean isEffect();

    /**
     * Get the amount of flashes that will occur before the lightning is
     * removed. By default this value is between 1 and 3.
     *
     * @return the flashes
     */
    public int getFlashes();

    /**
     * Set the amount of flashes that will occur before the lightning is
     * removed. One flash will occur after this lightning strike's life
     * has reduced below 0.
     *
     * @param flashes the flashes
     */
    public void setFlashes(int flashes);

    /**
     * Get the amount of ticks this lightning strike will inflict damage
     * upon its hit entities.
     * <p>
     * When life ticks are negative, there is a random chance that another
     * flash will be initiated and life ticks reset to 1.
     *
     * @return the life ticks
     */
    public int getLifeTicks();

    /**
     * Get the amount of ticks this lightning strike will inflict damage
     * upon its hit entities.
     * <p>
     * When life ticks are negative, there is a random chance that another
     * flash will be initiated and life ticks reset to 1. Additionally, if
     * life ticks are set to 2 (the default value when a lightning strike
     * has been spawned), a list of events will occur:
     * <ul>
     *   <li>Impact sound effects will be played
     *   <li>Fire will be spawned (dependent on difficulty)
     *   <li>Lightning rods will be powered (if hit)
     *   <li>Copper will be stripped (if hit)
     *   <li>{@link GameEvent#LIGHTNING_STRIKE} will be dispatched
     * </ul>
     *
     * @param ticks the life ticks
     */
    public void setLifeTicks(int ticks);

    /**
     * Get the {@link Player} that caused this lightning to strike. This
     * will occur naturally if a trident enchanted with
     * {@link Enchantment#CHANNELING Channeling} were thrown at an entity
     * during a storm.
     *
     * @return the player
     */
    @Nullable
    public Player getCausingPlayer();

    /**
     * Set the {@link Player} that caused this lightning to strike.
     *
     * @param player the player
     */
    public void setCausingPlayer(@Nullable Player player);

}

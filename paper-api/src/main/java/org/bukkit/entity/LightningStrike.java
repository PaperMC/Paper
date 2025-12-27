package org.bukkit.entity;

import org.bukkit.GameEvent;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
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
     * @deprecated use {@link #getFlashCount()}
     */
    @Deprecated // Paper
    public int getFlashes();

    /**
     * Set the amount of flashes that will occur before the lightning is
     * removed. One flash will occur after this lightning strike's life
     * has reduced below 0.
     *
     * @param flashes the flashes
     * @deprecated use {@link #setFlashCount(int)}
     */
    @Deprecated // Paper
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

    // Spigot start
    /**
     * @deprecated Unsupported api
     */
    @Deprecated(forRemoval = true)
    public class Spigot extends Entity.Spigot {

        /**
         * Returns whether the strike is silent.
         *
         * @return whether the strike is silent.
         * @deprecated sound is now client side and cannot be removed
         */
        @Deprecated(since = "1.20.4", forRemoval = true)
        public boolean isSilent() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * @deprecated Unsupported api
     */
    @NotNull
    @Override
    @Deprecated(forRemoval = true)
    Spigot spigot();
    // Spigot end

    // Paper start
    /**
     * Returns the amount of flash iterations that will be done before the lightning dies.
     *
     * @see #getLifeTicks() for how long the current flash will last
     * @return amount of flashes that will be shown before the lightning dies
     */
    int getFlashCount();

    /**
     * Sets the amount of life iterations that will be done before the lightning dies.
     * Default number of flashes on creation is between 1-3.
     *
     * @param flashes amount of iterations that will be done before the lightning dies, must to be a positive number
     */
    void setFlashCount(int flashes);

    /**
     * Returns the potential entity that caused this lightning strike to spawn in the world.
     * <p>
     * As of implementing this method, only {@link Player}s are capable of causing a lightning strike, however as this
     * might change in future minecraft releases, this method does not guarantee a player as the cause of a lightning.
     * Consumers of this method should hence validate whether or not the entity is a player if they want to use player
     * specific methods through an {@code instanceOf} check.
     * </p>
     * <p>
     * A player is, as of implementing this method, responsible for a lightning, and will hence be returned here as
     * a cause, if they channeled a {@link Trident} to summon it or were explicitly defined as the cause of this
     * lightning through {@link #setCausingPlayer(Player)}.
     * </p>
     *
     * @return the entity that caused this lightning or null if the lightning was not caused by an entity (e.g. normal
     * weather)
     */
    @org.jetbrains.annotations.Nullable
    Entity getCausingEntity();
    // Paper end
}

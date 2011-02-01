
package org.bukkit.entity;

/**
 * Represents a living entity, such as a monster or player
 */
public interface LivingEntity extends Entity {
    /**
     * Gets the entitys health from 0-20, where 0 is dead and 20 is full
     *
     * @return Health represented from 0-20
     */
    public int getHealth();

    /**
     * Sets the entitys health from 0-20, where 0 is dead and 20 is full
     *
     * @param health New health represented from 0-20
     */
    public void setHealth(int health);

    /**
     * Throws an egg from the entity.
     */
    public Egg throwEgg();

    /**
     * Throws a snowball from the entity.
     */
    public Snowball throwSnowball();

    /**
     * Shoots an arrow from the entity.
     *
     * @return
     */
    public Arrow shootArrow();

    /**
     * Returns whether this entity is inside a vehicle.
     *
     * @return
     */
    public boolean isInsideVehicle();

    /**
     * Leave the current vehicle. If the entity is currently in a vehicle
     * (and is removed from it), true will be returned, otherwise false will
     * be returned.
     *
     * @return
     */
    public boolean leaveVehicle();

    /**
     * Get the vehicle that this player is inside. If there is no vehicle,
     * null will be returned.
     *
     * @return
     */
    public Vehicle getVehicle();

    /**
     * Returns the amount of air that this entity has remaining, in ticks
     *
     * @return Amount of air remaining
     */
    public int getRemainingAir();

    /**
     * Sets the amount of air that this entity has remaining, in ticks
     *
     * @param ticks Amount of air remaining
     */
    public void setRemainingAir(int ticks);

    /**
     * Returns the maximum amount of air this entity can have, in ticks
     *
     * @return Maximum amount of air
     */
    public int getMaximumAir();

    /**
     * Sets the maximum amount of air this entity can have, in ticks
     *
     * @param ticks Maximum amount of air
     */
    public void setMaximumAir(int ticks);
}

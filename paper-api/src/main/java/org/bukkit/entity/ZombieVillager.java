package org.bukkit.entity;

/**
 * Represents a {@link Zombie} which was once a {@link Villager}.
 */
public interface ZombieVillager extends Zombie {

    /**
     * Sets the villager profession of this zombie.
     */
    @Override
    void setVillagerProfession(Villager.Profession profession);

    /**
     * Returns the villager profession of this zombie.
     *
     * @return the profession or null
     */
    @Override
    Villager.Profession getVillagerProfession();

    /**
     * Get if this entity is in the process of converting to a Villager as a
     * result of being cured.
     *
     * @return conversion status
     */
    @Override
    boolean isConverting();

    /**
     * Gets the amount of ticks until this entity will be converted to a
     * Villager as a result of being cured.
     *
     * When this reaches 0, the entity will be converted.
     *
     * @return conversion time
     * @throws IllegalStateException if {@link #isConverting()} is false.
     */
    @Override
    int getConversionTime();

    /**
     * Sets the amount of ticks until this entity will be converted to a
     * Villager as a result of being cured.
     *
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time new conversion time
     */
    @Override
    void setConversionTime(int time);
}

package org.bukkit.entity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Zombie.
 */
public interface Zombie extends Monster, Ageable {

    /**
     * Gets whether the zombie is a baby
     *
     * @return Whether the zombie is a baby
     * @deprecated see {@link Ageable#isAdult()}
     */
    @Deprecated
    public boolean isBaby();

    /**
     * Sets whether the zombie is a baby
     *
     * @param flag Whether the zombie is a baby
     * @deprecated see {@link Ageable#setBaby()} and {@link Ageable#setAdult()}
     */
    @Deprecated
    public void setBaby(boolean flag);

    /**
     * Gets whether the zombie is a villager
     *
     * @return Whether the zombie is a villager
     * @deprecated check if instanceof {@link ZombieVillager}.
     */
    @Deprecated
    public boolean isVillager();

    /**
     * @param flag flag
     * @deprecated must spawn {@link ZombieVillager}.
     */
    @Deprecated
    @Contract("_ -> fail")
    public void setVillager(boolean flag);

    /**
     * @param profession profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated
    @Contract("_ -> fail")
    public void setVillagerProfession(Villager.Profession profession);

    /**
     * @return profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated
    @Nullable
    @Contract("-> null")
    public Villager.Profession getVillagerProfession();

    /**
     * Get if this entity is in the process of converting to a Drowned as a
     * result of being underwater.
     *
     * @return conversion status
     */
    boolean isConverting();

    /**
     * Gets the amount of ticks until this entity will be converted to a Drowned
     * as a result of being underwater.
     *
     * When this reaches 0, the entity will be converted.
     *
     * @return conversion time
     * @throws IllegalStateException if {@link #isConverting()} is false.
     */
    int getConversionTime();

    /**
     * Sets the amount of ticks until this entity will be converted to a Drowned
     * as a result of being underwater.
     *
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time new conversion time
     */
    void setConversionTime(int time);

    /**
     * Gets whether this zombie can break doors
     *
     * @return Whether this zombie can break doors
     */
    boolean canBreakDoors();

    /**
     * Sets whether this zombie can break doors
     *
     * This will be ignored if the entity is a Drowned. Will also stop the action if
     * the entity is currently breaking a door.
     *
     * @param flag Whether this zombie can break doors
     */
    void setCanBreakDoors(boolean flag);
}

package org.bukkit.entity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Zombie.
 *
 * @since 1.0.0
 */
public interface Zombie extends Monster, Ageable {

    /**
     * Gets whether the zombie is a baby
     *
     * @return Whether the zombie is a baby
     * @deprecated see {@link Ageable#isAdult()}
     * @since 1.4.5
     */
    @Deprecated(since = "1.16.2")
    public boolean isBaby();

    /**
     * Sets whether the zombie is a baby
     *
     * @param flag Whether the zombie is a baby
     * @deprecated see {@link Ageable#setBaby()} and {@link Ageable#setAdult()}
     * @since 1.4.5
     */
    @Deprecated(since = "1.16.2")
    public void setBaby(boolean flag);

    /**
     * Gets whether the zombie is a villager
     *
     * @return Whether the zombie is a villager
     * @deprecated check if instanceof {@link ZombieVillager}.
     * @since 1.4.5
     */
    @Deprecated(since = "1.10.2")
    public boolean isVillager();

    /**
     * @param flag flag
     * @deprecated must spawn {@link ZombieVillager}.
     * @since 1.4.5
     */
    @Deprecated(since = "1.9")
    @Contract("_ -> fail")
    public void setVillager(boolean flag);

    /**
     * @param profession profession
     * @see ZombieVillager#getVillagerProfession()
     */
    @Deprecated(since = "1.10.2")
    @Contract("_ -> fail")
    public void setVillagerProfession(Villager.Profession profession);

    /**
     * @return profession
     * @see ZombieVillager#getVillagerProfession()
     * @since 1.9.4
     */
    @Deprecated(since = "1.10.2")
    @Nullable
    @Contract("-> null")
    public Villager.Profession getVillagerProfession();

    /**
     * Get if this entity is in the process of converting to a Drowned as a
     * result of being underwater.
     *
     * @return conversion status
     * @since 1.13.2
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
     * @since 1.13.2
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
     * @since 1.13.2
     */
    void setConversionTime(int time);

    /**
     * Gets whether this zombie can break doors
     *
     * @return Whether this zombie can break doors
     * @since 1.16.4
     */
    boolean canBreakDoors();

    /**
     * Sets whether this zombie can break doors
     * <p>
     * Check {@link #supportsBreakingDoors()} to see
     * if this zombie type will even be affected by using
     * this method. Will also stop the action if
     * the entity is currently breaking a door.
     *
     * @param flag Whether this zombie can break doors
     * @since 1.16.4
     */
    void setCanBreakDoors(boolean flag);

    // Paper start
    /**
     * Check if zombie is drowning
     *
     * @return True if zombie conversion process has begun
     * @since 1.13.2
     */
    boolean isDrowning();

    /**
     * Make zombie start drowning
     *
     * @param drownedConversionTime Amount of time until zombie converts from drowning
     *
     * @deprecated See {@link #setConversionTime(int)}
     * @since 1.13.2
     */
    @Deprecated
    void startDrowning(int drownedConversionTime);

    /**
     * Stop a zombie from starting the drowning conversion process
     *
     * @since 1.13.2
     */
    void stopDrowning();

    /**
     * Set if zombie has its arms raised
     *
     * @param raised True to raise arms
     * @deprecated use {{@link #setAggressive(boolean)}}
     * @since 1.13.2
     */
    @Deprecated
    void setArmsRaised(boolean raised);

    /**
     * Check if zombie has arms raised
     *
     * @return True if arms are raised
     * @deprecated use {@link #isAggressive()}
     * @since 1.13.2
     */
    @Deprecated
    boolean isArmsRaised();

    /**
     * Check if this zombie will burn in the sunlight
     *
     * @return True if zombie will burn in sunlight
     * @since 1.13.2
     */
    boolean shouldBurnInDay();

    /**
     * Set if this zombie should burn in the sunlight
     *
     * @param shouldBurnInDay True to burn in sunlight
     * @since 1.13.2
     */
    void setShouldBurnInDay(boolean shouldBurnInDay);

    /**
     * Checks if this zombie type supports breaking doors.
     * {@link Drowned} do not have support for breaking doors
     * so using {@link #setCanBreakDoors(boolean)} on them has
     * no effect.
     *
     * @return true if entity supports breaking doors
     * @deprecated Since 1.21.2 all zombie types can break doors if instructed as MC-137053 was fixed.
     */
    @Deprecated(since = "1.21.2", forRemoval = true)
    boolean supportsBreakingDoors();
    // Paper end
}

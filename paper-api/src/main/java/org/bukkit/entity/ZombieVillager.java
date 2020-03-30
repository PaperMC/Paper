package org.bukkit.entity;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Zombie} which was once a {@link Villager}.
 */
public interface ZombieVillager extends Zombie {

    /**
     * Sets the villager profession of this zombie.
     */
    @Override
    void setVillagerProfession(@Nullable Villager.Profession profession);

    /**
     * Returns the villager profession of this zombie.
     *
     * @return the profession or null
     */
    @Override
    @Nullable
    Villager.Profession getVillagerProfession();

    /**
     * Gets the current type of this villager.
     *
     * @return Current type.
     */
    @NotNull
    public Villager.Type getVillagerType();

    /**
     * Sets the new type of this villager.
     *
     * @param type New type.
     */
    public void setVillagerType(@NotNull Villager.Type type);

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

    /**
     * Gets the player who initiated the conversion.
     *
     * @return the player, or <code>null</code> if the player is unknown or the
     * entity isn't converting currently
     */
    @Nullable
    OfflinePlayer getConversionPlayer();

    /**
     * Sets the player who initiated the conversion.
     * <p>
     * This has no effect if this entity isn't converting currently.
     *
     * @param conversionPlayer the player
     */
    void setConversionPlayer(@Nullable OfflinePlayer conversionPlayer);
}

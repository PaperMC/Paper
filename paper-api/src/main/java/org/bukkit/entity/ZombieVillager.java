package org.bukkit.entity;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Zombie} which was once a {@link Villager}.
 *
 * @since 1.11
 */
public interface ZombieVillager extends Zombie {

    /**
     * Sets the villager profession of this zombie.
     */
    @Override
    void setVillagerProfession(@NotNull Villager.Profession profession); // Paper

    /**
     * Returns the villager profession of this zombie.
     *
     * @return the profession
     */
    @Override
    @NotNull // Paper
    Villager.Profession getVillagerProfession();

    /**
     * Gets the current type of this villager.
     *
     * @return Current type.
     * @since 1.15.2
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
     * @since 1.13.2
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
     * @since 1.13.2
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
     * @since 1.13.2
     */
    @Override
    void setConversionTime(int time);

    /**
     * Gets the player who initiated the conversion.
     *
     * @return the player, or <code>null</code> if the player is unknown or the
     * entity isn't converting currently
     * @since 1.14.1
     */
    @Nullable
    OfflinePlayer getConversionPlayer();

    /**
     * Sets the player who initiated the conversion.
     * <p>
     * This has no effect if this entity isn't converting currently.
     *
     * @param conversionPlayer the player
     * @since 1.14.1
     */
    void setConversionPlayer(@Nullable OfflinePlayer conversionPlayer);

    // Paper start - missing entity behaviour api - converting without entity event
    /**
     * Sets the amount of ticks until this entity will be converted to a
     * Villager as a result of being cured.
     * <p>
     * When this reaches 0, the entity will be converted. A value of less than 0
     * will stop the current conversion process without converting the current
     * entity.
     *
     * @param time new conversion time
     * @param broadcastEntityEvent whether this conversion time mutation should broadcast the
     *                             org.bukkit.{@link org.bukkit.EntityEffect#ZOMBIE_TRANSFORM} entity event to the
     *                             world. If false, no entity event is published, preventing for example the
     *                             org.bukkit.{@link org.bukkit.Sound#ENTITY_ZOMBIE_VILLAGER_CURE} from playing.
     * @since 1.19
     */
    void setConversionTime(int time, boolean broadcastEntityEvent);
    // Paper end - missing entity behaviour api - converting without entity event
}

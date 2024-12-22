package org.bukkit.entity;

import org.bukkit.Sound;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Mob. Mobs are living entities with simple AI.
 */
public interface Mob extends LivingEntity, Lootable, io.papermc.paper.entity.Leashable { // Paper - Leashable API

    // Paper start
    @Override
    org.bukkit.inventory.@org.jetbrains.annotations.NotNull EntityEquipment getEquipment();

    /**
     * Enables access to control the pathing of an Entity
     * @return Pathfinding Manager for this entity
     */
    @NotNull
    com.destroystokyo.paper.entity.Pathfinder getPathfinder();

    /**
     * Check if this mob is exposed to daylight
     *
     * @return True if mob is exposed to daylight
     */
    boolean isInDaylight();

    /**
     * Instruct this Mob to look at a specific Location
     * <p>
     * Useful when implementing custom mob goals
     *
     * @param location location to look at
     */
    void lookAt(@NotNull org.bukkit.Location location);

    /**
     * Instruct this Mob to look at a specific Location
     * <p>
     * Useful when implementing custom mob goals
     *
     * @param location location to look at
     * @param headRotationSpeed head rotation speed
     * @param maxHeadPitch max head pitch rotation
     */
    void lookAt(@NotNull org.bukkit.Location location, float headRotationSpeed, float maxHeadPitch);

    /**
     * Instruct this Mob to look at a specific Entity
     * <p>
     * If a LivingEntity, look at eye location
     * <p>
     * Useful when implementing custom mob goals
     *
     * @param entity entity to look at
     */
    void lookAt(@NotNull Entity entity);

    /**
     * Instruct this Mob to look at a specific Entity
     * <p>
     * If a LivingEntity, look at eye location
     * <p>
     * Useful when implementing custom mob goals
     *
     * @param entity entity to look at
     * @param headRotationSpeed head rotation speed
     * @param maxHeadPitch max head pitch rotation
     */
    void lookAt(@NotNull Entity entity, float headRotationSpeed, float maxHeadPitch);

    /**
     * Instruct this Mob to look at a specific position
     * <p>
     * Useful when implementing custom mob goals
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    void lookAt(double x, double y, double z);

    /**
     * Instruct this Mob to look at a specific position
     * <p>
     * Useful when implementing custom mob goals
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param headRotationSpeed head rotation speed
     * @param maxHeadPitch max head pitch rotation
     */
    void lookAt(double x, double y, double z, float headRotationSpeed, float maxHeadPitch);

    /**
     * Gets the head rotation speed
     *
     * @return the head rotation speed
     */
    int getHeadRotationSpeed();

    /**
     * Gets the max head pitch rotation
     *
     * @return the max head pitch rotation
     */
    int getMaxHeadPitch();
    // Paper end
    /**
     * Instructs this Mob to set the specified LivingEntity as its target.
     * <p>
     * Hostile creatures may attack their target, and friendly creatures may
     * follow their target.
     *
     * @param target New LivingEntity to target, or null to clear the target
     */
    public void setTarget(@Nullable LivingEntity target);

    /**
     * Gets the current target of this Mob
     *
     * @return Current target of this creature, or null if none exists
     */
    @Nullable
    public LivingEntity getTarget();

    /**
     * Sets whether this mob is aware of its surroundings.
     *
     * Unaware mobs will still move if pushed, attacked, etc. but will not move
     * or perform any actions on their own. Unaware mobs may also have other
     * unspecified behaviours disabled, such as drowning.
     *
     * @param aware whether the mob is aware
     */
    public void setAware(boolean aware);

    /**
     * Gets whether this mob is aware of its surroundings.
     *
     * Unaware mobs will still move if pushed, attacked, etc. but will not move
     * or perform any actions on their own. Unaware mobs may also have other
     * unspecified behaviours disabled, such as drowning.
     *
     * @return whether the mob is aware
     */
    public boolean isAware();

    /**
     * Get the {@link Sound} this mob makes while ambiently existing. This sound
     * may change depending on the current state of the entity, and may also
     * return null under specific conditions. This sound is not constant.
     * For instance, villagers will make different passive noises depending
     * on whether or not they are actively trading with a player, or make no
     * ambient noise while sleeping.
     *
     * @return the ambient sound, or null if this entity is ambiently quiet
     */
    @Nullable
    public Sound getAmbientSound();

    // Paper start - LootTable API
    @Override
    default void setLootTable(final @Nullable org.bukkit.loot.LootTable table, final long seed) {
        this.setLootTable(table);
        this.setSeed(seed);
    }
    // Paper end - LootTable API

    // Paper start - Missing Entity API
    /**
     * Some mobs will raise their arm(s) when aggressive:
     * <ul>
     *     <li>{@link Drowned}</li>
     *     <li>{@link Piglin}</li>
     *     <li>{@link Skeleton}</li>
     *     <li>{@link Zombie}</li>
     *     <li>{@link ZombieVillager}</li>
     *     <li>{@link Illusioner}</li>
     *     <li>{@link Vindicator}</li>
     *     <li>{@link Panda}</li>
     *     <li>{@link Pillager}</li>
     *     <li>{@link PiglinBrute}</li>
     * </ul>
     * <p>
     * Note: This doesn't always show the actual aggressive state as
     * set by {@link #setAggressive(boolean)}. {@link Panda}'s are always
     * aggressive if their combined {@link Panda.Gene} is {@link Panda.Gene#AGGRESSIVE}.
     *
     * @return whether the mob is aggressive or not
     */
    boolean isAggressive();

    /**
     * Some mobs will raise their arm(s) when aggressive,
     * see {@link #isAggressive()} for full list.
     *
     * @param aggressive whether the mob should be aggressive or not
     * @see #isAggressive()
     */
    void setAggressive(boolean aggressive);
    // Paper end - Missing Entity API

    // Paper start - left-handed API
    /**
     * Check if Mob is left-handed
     *
     * @return True if left-handed
     */
    public boolean isLeftHanded();

    /**
      * Set if Mob is left-handed
      *
      * @param leftHanded True if left-handed
      */
    public void setLeftHanded(boolean leftHanded);
    // Paper end - left-handed API

    // Paper start - mob xp reward API
    /**
     * Gets the amount of experience the mob will possibly drop. This value is randomized and it can give different results
     *
     * @return the amount of experience the mob will possibly drop
     */
    public int getPossibleExperienceReward();
    // Paper end - mob xp reward API
}

package org.bukkit.entity;

import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Firework extends Projectile {

    /**
     * Get a copy of the fireworks meta
     *
     * @return A copy of the current Firework meta
     */
    @NotNull
    FireworkMeta getFireworkMeta();

    /**
     * Apply the provided meta to the fireworks
     * <p>
     * Adjusts detonation ticks automatically.
     *
     * @param meta The FireworkMeta to apply
     */
    void setFireworkMeta(@NotNull FireworkMeta meta);

    /**
     * Set the {@link LivingEntity} to which this firework is attached.
     * <p>
     * When attached to an entity, the firework effect will act as normal but
     * remain positioned on the entity. If the entity {@code LivingEntity#isGliding()
     * is gliding}, then the entity will receive a boost in the direction that
     * they are looking.
     *
     * @param entity the entity to which the firework should be attached, or
     * null to remove the attached entity
     * @return true if the entity could be attached, false if the firework was
     * already detonated
     */
    boolean setAttachedTo(@Nullable LivingEntity entity);

    /**
     * Get the {@link LivingEntity} to which this firework is attached.
     * <p>
     * When attached to an entity, the firework effect will act as normal but
     * remain positioned on the entity. If the entity {@code LivingEntity#isGliding()
     * is gliding}, then the entity will receive a boost in the direction that
     * they are looking.
     *
     * @return the attached entity, or null if none
     */
    @Nullable
    LivingEntity getAttachedTo();

    /**
     * Set the ticks that this firework has been alive. If this value exceeds
     * {@link #getMaxLife()}, the firework will detonate.
     *
     * @param ticks the ticks to set. Must be greater than or equal to 0
     * @deprecated use {@link #setTicksFlown(int)}
     * @return true if the life was set, false if this firework has already detonated
     */
    @Deprecated(forRemoval = true) // Paper
    boolean setLife(int ticks);

    /**
     * Get the ticks that this firework has been alive. When this value reaches
     * {@link #getMaxLife()}, the firework will detonate.
     *
     * @deprecated use {@link #getTicksFlown()}
     * @return the life ticks
     */
    @Deprecated(forRemoval = true) // Paper
    int getLife();

    /**
     * Set the time in ticks this firework will exist until it is detonated.
     *
     * @param ticks the ticks to set. Must be greater than 0
     * @deprecated use {@link #setTicksToDetonate(int)}
     * @return true if the time was set, false if this firework has already detonated
     */
    @Deprecated(forRemoval = true) // Paper
    boolean setMaxLife(int ticks);

    /**
     * Get the time in ticks this firework will exist until it is detonated.
     *
     * @deprecated use {@link #getTicksToDetonate()}
     * @return the maximum life in ticks
     */
    @Deprecated(forRemoval = true) // Paper
    int getMaxLife();

    /**
     * Cause this firework to explode at earliest opportunity, as if it has no
     * remaining fuse.
     */
    void detonate();

    /**
     * Check whether or not this firework has detonated.
     *
     * @return true if detonated, false if still in the world
     */
    boolean isDetonated();

    /**
     * Gets if the firework was shot at an angle (i.e. from a crossbow).
     *
     * A firework which was not shot at an angle will fly straight upwards.
     *
     * @return shot at angle status
     */
    boolean isShotAtAngle();

    /**
     * Sets if the firework was shot at an angle (i.e. from a crossbow).
     *
     * A firework which was not shot at an angle will fly straight upwards.
     *
     * @param shotAtAngle the new shotAtAngle
     */
    void setShotAtAngle(boolean shotAtAngle);

    // Paper start
    @org.jetbrains.annotations.Nullable
    public java.util.UUID getSpawningEntity();
    /**
     * If this firework is boosting an entity, return it
     * @deprecated use {@link #getAttachedTo()}
     * @see #setAttachedTo(LivingEntity)
     * @return The entity being boosted
     */
    @org.jetbrains.annotations.Nullable
    @Deprecated
    default LivingEntity getBoostedEntity() {
        return getAttachedTo();
    }
    // Paper end

    // Paper start - Firework API
    /**
     * Gets the item used in the firework.
     *
     * @return firework item
     */
    @NotNull
    public org.bukkit.inventory.ItemStack getItem();

    /**
     * Sets the item used in the firework.
     * <p>
     * Firework explosion effects are used from this item.
     *
     * @param itemStack item to set
     */
    void setItem(@org.jetbrains.annotations.Nullable org.bukkit.inventory.ItemStack itemStack);

    /**
     * Gets the number of ticks the firework has flown.
     *
     * @return ticks flown
     */
    int getTicksFlown();

    /**
     * Sets the number of ticks the firework has flown.
     * Setting this greater than detonation ticks will cause the firework to explode.
     *
     * @param ticks ticks flown
     */
    void setTicksFlown(int ticks);

    /**
     * Gets the number of ticks the firework will detonate on.
     *
     * @return the tick to detonate on
     */
    int getTicksToDetonate();

    /**
     * Set the amount of ticks the firework will detonate on.
     *
     * @param ticks ticks to detonate on
     */
    void setTicksToDetonate(int ticks);
    // Paper end
}

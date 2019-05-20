package org.bukkit.entity;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an area effect cloud which will imbue a potion effect onto
 * entities which enter it.
 */
public interface AreaEffectCloud extends Entity {

    /**
     * Gets the duration which this cloud will exist for (in ticks).
     *
     * @return cloud duration
     */
    int getDuration();

    /**
     * Sets the duration which this cloud will exist for (in ticks).
     *
     * @param duration cloud duration
     */
    void setDuration(int duration);

    /**
     * Gets the time which an entity has to be exposed to the cloud before the
     * effect is applied.
     *
     * @return wait time
     */
    int getWaitTime();

    /**
     * Sets the time which an entity has to be exposed to the cloud before the
     * effect is applied.
     *
     * @param waitTime wait time
     */
    void setWaitTime(int waitTime);

    /**
     * Gets the time that an entity will be immune from subsequent exposure.
     *
     * @return reapplication delay
     */
    int getReapplicationDelay();

    /**
     * Sets the time that an entity will be immune from subsequent exposure.
     *
     * @param delay reapplication delay
     */
    void setReapplicationDelay(int delay);

    /**
     * Gets the amount that the duration of this cloud will decrease by when it
     * applies an effect to an entity.
     *
     * @return duration on use delta
     */
    int getDurationOnUse();

    /**
     * Sets the amount that the duration of this cloud will decrease by when it
     * applies an effect to an entity.
     *
     * @param duration duration on use delta
     */
    void setDurationOnUse(int duration);

    /**
     * Gets the initial radius of the cloud.
     *
     * @return radius
     */
    float getRadius();

    /**
     * Sets the initial radius of the cloud.
     *
     * @param radius radius
     */
    void setRadius(float radius);

    /**
     * Gets the amount that the radius of this cloud will decrease by when it
     * applies an effect to an entity.
     *
     * @return radius on use delta
     */
    float getRadiusOnUse();

    /**
     * Sets the amount that the radius of this cloud will decrease by when it
     * applies an effect to an entity.
     *
     * @param radius radius on use delta
     */
    void setRadiusOnUse(float radius);

    /**
     * Gets the amount that the radius of this cloud will decrease by each tick.
     *
     * @return radius per tick delta
     */
    float getRadiusPerTick();

    /**
     * Gets the amount that the radius of this cloud will decrease by each tick.
     *
     * @param radius per tick delta
     */
    void setRadiusPerTick(float radius);

    /**
     * Gets the particle which this cloud will be composed of
     *
     * @return particle the set particle type
     */
    @NotNull
    Particle getParticle();

    /**
     * Sets the particle which this cloud will be composed of
     *
     * @param particle the new particle type
     */
    void setParticle(@NotNull Particle particle);

    /**
     * Sets the particle which this cloud will be composed of
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the new particle type
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    <T> void setParticle(@NotNull Particle particle, @Nullable T data);

    /**
     * Sets the underlying potion data
     *
     * @param data PotionData to set the base potion state to
     */
    void setBasePotionData(@NotNull PotionData data);

    /**
     * Returns the potion data about the base potion
     *
     * @return a PotionData object
     */
    @NotNull
    PotionData getBasePotionData();

    /**
     * Checks for the presence of custom potion effects.
     *
     * @return true if custom potion effects are applied
     */
    boolean hasCustomEffects();

    /**
     * Gets an immutable list containing all custom potion effects applied to
     * this cloud.
     * <p>
     * Plugins should check that hasCustomEffects() returns true before calling
     * this method.
     *
     * @return the immutable list of custom potion effects
     */
    @NotNull
    List<PotionEffect> getCustomEffects();

    /**
     * Adds a custom potion effect to this cloud.
     *
     * @param effect the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the effect was added as a result of this call
     */
    boolean addCustomEffect(@NotNull PotionEffect effect, boolean overwrite);

    /**
     * Removes a custom potion effect from this cloud.
     *
     * @param type the potion effect type to remove
     * @return true if the an effect was removed as a result of this call
     */
    boolean removeCustomEffect(@NotNull PotionEffectType type);

    /**
     * Checks for a specific custom potion effect type on this cloud.
     *
     * @param type the potion effect type to check for
     * @return true if the potion has this effect
     */
    boolean hasCustomEffect(@Nullable PotionEffectType type);

    /**
     * Removes all custom potion effects from this cloud.
     */
    void clearCustomEffects();

    /**
     * Gets the color of this cloud. Will be applied as a tint to its particles.
     *
     * @return cloud color
     */
    @NotNull
    Color getColor();

    /**
     * Sets the color of this cloud. Will be applied as a tint to its particles.
     *
     * @param color cloud color
     */
    void setColor(@NotNull Color color);

    /**
     * Retrieve the original source of this cloud.
     *
     * @return the {@link ProjectileSource} that threw the LingeringPotion
     */
    @Nullable
    public ProjectileSource getSource();

    /**
     * Set the original source of this cloud.
     *
     * @param source the {@link ProjectileSource} that threw the LingeringPotion
     */
    public void setSource(@Nullable ProjectileSource source);
}

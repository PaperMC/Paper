package org.bukkit.entity;

import java.util.List;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;

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
    Particle getParticle();

    /**
     * Sets the particle which this cloud will be composed of
     *
     * @param particle the new particle type
     */
    void setParticle(Particle particle);

    /**
     * Get a copy of all effects which can be applied by this cloud. No
     * guarantees are made about the nature of the returned list.
     *
     * @return the list of all effects
     */
    List<PotionEffect> getEffects();

    /**
     * Add an effect which can be applied by this cloud.
     *
     * @param effect the effect to add
     */
    void addEffect(PotionEffect effect);

    /**
     * Remove an effect from this cloud.
     *
     * @param effect the effect to remove
     */
    void removeEffect(PotionEffect effect);

    /**
     * Set the effects of this cloud. Will remove all existing effects.
     *
     * @param effects the new effects to set
     */
    void setEffects(List<PotionEffect> effects);

    /**
     * Gets the color of this cloud. Will be applied as a tint to its particles.
     *
     * @return cloud color
     */
    Color getColor();

    /**
     * Sets the color of this cloud. Will be applied as a tint to its particles.
     *
     * @param color cloud color
     */
    void setColor(Color color);
}

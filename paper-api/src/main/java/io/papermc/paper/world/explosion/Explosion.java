package io.papermc.paper.world.explosion;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents an explosion.
 */
@NullMarked
public interface Explosion {

    /**
     * Explode the explosion.
     * @return false if explosion was canceled, otherwise true
     */
    boolean explode();

    /**
     * {@return the source entity of the explosion, or null if there is no source}
     */
    @Nullable Entity source();

    /**
     * {@return the center of the explosion}
     */
    Location center();

    /**
     * {@return the radius of the explosion}
     */
    float radius();

    /**
     * {@return whether the explosion should break blocks}
     */
    boolean breakBlocks();

    /**
     * {@return whether the explosion should set fire to blocks}
     */
    boolean setFire();

    /**
     * {@return the small explosion particle, or null if there is no particle}
     */
    @Nullable ParticleBuilder smallExplosionParticle();

    /**
     * {@return the large explosion particle, or null if there is no particle}
     */
    @Nullable ParticleBuilder largeExplosionParticle();

    /**
     * {@return the sound to play when the explosion occurs, or null if there is no sound}
     */
    @Nullable Sound sound();

    /**
     * {@return whether the source of the explosion should be excluded from damage}
     */
    boolean excludeSourceFromDamage();

    /**
     * {@return the damage calculator for the explosion, or null to use the default damage calculator}
     */
    @Nullable DamageCalculator damageCalculator();

    /**
     * {@return a new explosion builder}
     */
    @Contract(value = "-> new", pure = true)
    static Builder builder() {
        return Bukkit.createExplosion();
    }

    interface Builder {

        /**
         * {@return the explosion}
         */
        Explosion build();

        /**
         * Set the source entity of the explosion. This may cause side effects (spawn only one particle at the center, or prevent breaking blocks if {@link org.bukkit.GameRule#MOB_GRIEFING} is set to false).
         * @param source The source entity of the explosion
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder source(Entity source);

        /**
         * Set the center of the explosion.
         * @param center The center of the explosion
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder center(Location center);

        /**
         * Set the radius of the explosion, where 4F is TNT.
         * @param radius The radius of the explosion
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder radius(float radius);

        /**
         * Set whether the explosion should break blocks.
         * @param breakBlocks Whether the explosion should break blocks
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder breakBlocks(boolean breakBlocks);

        /**
         * Set whether the explosion should set fire to blocks.
         * @param setFire Whether the explosion should set fire to blocks
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder setFire(boolean setFire);

        /**
         * Set the particle to use for the small explosion. Only {@link ParticleBuilder#data()} will be used.
         * @param smallExplosionParticle The particle to use for the small explosion, or null to use no particle
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder smallExplosionParticle(@Nullable ParticleBuilder smallExplosionParticle);

        /**
         * Set the particle to use for the large explosion. Only {@link ParticleBuilder#data()} will be used.
         * @param largeExplosionParticle The particle to use for the large explosion, or null to use no particle
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder largeExplosionParticle(@Nullable ParticleBuilder largeExplosionParticle);

        /**
         * Set the sound to play when the explosion occurs.
         * @param sound The sound to play, or null to play no sound
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(@Nullable Sound sound);

        /**
         * Set whether the source of the explosion should be excluded from damage.
         * @param excludeSourceFromDamage Whether the source of the explosion should be excluded from damage
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder excludeSourceFromDamage(boolean excludeSourceFromDamage);

        /**
         * Set the damage calculator for the explosion.
         * @param damageCalculator The damage calculator for the explosion, or null to use the default damage calculator
         * @return The builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder damageProvider(@Nullable DamageCalculator damageCalculator);
    }

    @FunctionalInterface
    interface DamageCalculator {
        /**
         * Calculate the damage to an entity.
         * @param explosion The explosion
         * @param entity The entity
         * @param seenPercent Percentage of the entity exposed to the explosion
         * @return The damage to the entity
         */
        float getDamage(Explosion explosion, Entity entity, float seenPercent);
    }
}

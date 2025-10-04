package io.papermc.paper.world.explosion;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.base.Preconditions;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Explosion(
    @Nullable Entity source,
    @NotNull Location center,
    float radius,
    boolean breakBlocks,
    boolean setFire,
    @Nullable ParticleBuilder smallExplosionParticle,
    @Nullable ParticleBuilder largeExplosionParticle,
    @Nullable Sound sound,
    boolean excludeSourceFromDamage,
    @Nullable BlockExplosionResistanceProvider blockExplosionResistanceProvider,
    @Nullable BlockFilter blockFilter,
    @Nullable EntityFilter entityFilter,
    @Nullable KnockbackMultiplierProvider knockbackMultiplierProvider,
    @Nullable DamageProvider damageProvider) {

    /**
     * Explode the explosion.
     * @return false if explosion was canceled, otherwise true
     */
    public boolean explode() {
        return center.getWorld().createExplosion(this);
    }

    /**
     * {@return a new explosion builder}
     */
    public static Builder explosion() {
        return new Builder();
    }

    public static class Builder {
        private Entity source = null;
        private Location center;
        private float radius = 4;
        private boolean breakBlocks = true;
        private boolean setFire = true;
        private ParticleBuilder smallExplosionParticle = Particle.EXPLOSION.builder();
        private ParticleBuilder largeExplosionParticle = Particle.EXPLOSION_EMITTER.builder();
        private Sound sound = Sound.ENTITY_GENERIC_EXPLODE;
        private boolean excludeSourceFromDamage = true;
        private BlockExplosionResistanceProvider blockExplosionResistanceProvider;
        private BlockFilter blockFilter;
        private EntityFilter entityFilter;
        private KnockbackMultiplierProvider knockbackMultiplierProvider;
        private DamageProvider damageProvider;

        private Builder() {
        }

        /**
         * {@return the explosion}
         */
        public Explosion build() {
            Preconditions.checkNotNull(center, "Explosion center cannot be null");
            return new Explosion(
                source,
                center,
                radius,
                breakBlocks,
                setFire,
                smallExplosionParticle,
                largeExplosionParticle,
                sound,
                excludeSourceFromDamage,
                blockExplosionResistanceProvider,
                blockFilter,
                entityFilter,
                knockbackMultiplierProvider,
                damageProvider
            );
        }

        /**
         * Set the source entity of the explosion.
         * @param source The source entity of the explosion
         * @return The builder
         */
        public Builder source(Entity source) {
            this.source = source;
            return this;
        }

        /**
         * Set the center of the explosion.
         * @param center The center of the explosion
         * @return The builder
         */
        public Builder center(Location center) {
            this.center = center;
            return this;
        }

        /**
         * Set the radius of the explosion, where 4F is TNT.
         * @param radius The radius of the explosion
         * @return The builder
         */
        public Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        /**
         * Set whether the explosion should break blocks.
         * @param breakBlocks Whether the explosion should break blocks
         * @return The builder
         */
        public Builder breakBlocks(boolean breakBlocks) {
            this.breakBlocks = breakBlocks;
            return this;
        }

        /**
         * Set whether the explosion should set fire to blocks.
         * @param setFire Whether the explosion should set fire to blocks
         * @return The builder
         */
        public Builder setFire(boolean setFire) {
            this.setFire = setFire;
            return this;
        }

        /**
         * Set the particle to use for the small explosion. Only {@link ParticleBuilder#data()} will be used.
         * @param smallExplosionParticle The particle to use for the small explosion, or null to use no particle
         * @return The builder
         */
        public Builder smallExplosionParticle(@Nullable ParticleBuilder smallExplosionParticle) {
            this.smallExplosionParticle = smallExplosionParticle;
            return this;
        }

        /**
         * Set the particle to use for the large explosion. Only {@link ParticleBuilder#data()} will be used.
         * @param largeExplosionParticle The particle to use for the large explosion, or null to use no particle
         * @return The builder
         */
        public Builder largeExplosionParticle(@Nullable ParticleBuilder largeExplosionParticle) {
            this.largeExplosionParticle = largeExplosionParticle;
            return this;
        }

        /**
         * Set the sound to play when the explosion occurs.
         * @param sound The sound to play, or null to play no sound
         * @return The builder
         */
        public Builder sound(@Nullable Sound sound) {
            this.sound = sound;
            return this;
        }

        /**
         * Set whether the source of the explosion should be excluded from damage.
         * @param excludeSourceFromDamage Whether the source of the explosion should be excluded from damage
         * @return The builder
         */
        public Builder excludeSourceFromDamage(boolean excludeSourceFromDamage) {
            this.excludeSourceFromDamage = excludeSourceFromDamage;
            return this;
        }

        /**
         * Set the block explosion resistance provider.
         * @param blockExplosionResistanceProvider The block explosion resistance provider, or null to use the default block explosion resistance provider
         * @return The builder
         */
        public Builder blockExplosionResistanceProvider(@Nullable BlockExplosionResistanceProvider blockExplosionResistanceProvider) {
            this.blockExplosionResistanceProvider = blockExplosionResistanceProvider;
            return this;
        }

        /**
         * Set the block explode filter.
         * @param blockFilter The block explode filter, or null to use the default block explode filter
         * @return The builder
         */
        public Builder blockExplodeFilter(@Nullable BlockFilter blockFilter) {
            this.blockFilter = blockFilter;
            return this;
        }

        /**
         * Set the damage entity filter.
         * @param entityFilter The damage entity filter, or null to use the default damage entity filter
         * @return The builder
         */
        public Builder damageEntityFilter(@Nullable EntityFilter entityFilter) {
            this.entityFilter = entityFilter;
            return this;
        }

        /**
         * Set the knockback multiplier provider.
         * @param knockbackMultiplierProvider The knockback multiplier provider, or null to use the default knockback multiplier provider
         * @return The builder
         */
        public Builder knockbackMultiplierProvider(@Nullable KnockbackMultiplierProvider knockbackMultiplierProvider) {
            this.knockbackMultiplierProvider = knockbackMultiplierProvider;
            return this;
        }

        /**
         * Set the damage provider for the explosion.
         * @param damageProvider The damage provider for the explosion, or null to use the default damage provider
         * @return The builder
         */
        public Builder damageProvider(@Nullable DamageProvider damageProvider) {
            this.damageProvider = damageProvider;
            return this;
        }
    }

    @FunctionalInterface
    public interface BlockExplosionResistanceProvider {
        /**
         * Calculate the block explosion resistance for a block.
         * @param explosion The explosion
         * @param block The block
         * @return The block explosion resistance, or null if the block has no explosion resistance, which is -0.3
         */
        Optional<Float> getBlockExplosionResistance(Explosion explosion, Block block);
    }

    @FunctionalInterface
    public interface BlockFilter {
        /**
         * Determine whether a block should be exploded.
         * @param explosion The explosion
         * @param block The block
         * @param power The power of the explosion, which is a random number between 0.7 and 1.3 times radius
         * @return Whether the block should be exploded
         */
        boolean shouldExplode(Explosion explosion, Block block, float power);
    }

    @FunctionalInterface
    public interface EntityFilter {
        /**
         * Determine whether an entity should be damaged by the explosion.
         * @param explosion The explosion
         * @param entity The entity
         * @return Whether the entity should be damaged
         */
        boolean shouldDamageEntity(Explosion explosion, Entity entity);
    }

    @FunctionalInterface
    public interface KnockbackMultiplierProvider {
        /**
         * Calculate the knockback multiplier for an entity.
         * @param explosion The explosion
         * @param entity The entity
         * @return The knockback multiplier for the entity
         */
        float getKnockbackMultiplier(Explosion explosion, Entity entity);
    }

    @FunctionalInterface
    public interface DamageProvider {
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

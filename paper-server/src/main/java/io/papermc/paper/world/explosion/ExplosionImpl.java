package io.papermc.paper.world.explosion;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record ExplosionImpl(
    @Nullable Entity source,
    Location center,
    float radius,
    boolean breakBlocks,
    boolean setFire,
    @Nullable ParticleBuilder smallExplosionParticle,
    @Nullable ParticleBuilder largeExplosionParticle,
    @Nullable Sound sound,
    boolean excludeSourceFromDamage,
    @Nullable DamageCalculator damageCalculator) implements Explosion {

    public boolean explode() {
        ServerLevel world = ((CraftWorld) center.getWorld()).getHandle();
        Level.ExplosionInteraction explosionType;
        if (!breakBlocks) {
            explosionType = Level.ExplosionInteraction.NONE;
        } else if (source == null) {
            explosionType = Level.ExplosionInteraction.STANDARD;
        } else {
            explosionType = Level.ExplosionInteraction.MOB;
        }

        net.minecraft.world.entity.Entity entity = source == null ? null : ((CraftEntity) source).getHandle();
        return !world.explode0(
            entity,
            net.minecraft.world.level.Explosion.getDefaultDamageSource(world, entity),
            new PaperExplosionDamageCalculator(this),
            center.x(),
            center.y(),
            center.z(),
            radius,
            setFire,
            explosionType,
            smallExplosionParticle == null
                ? new DustParticleOptions(0, 0.0F)
                : CraftParticle.createParticleParam(
                smallExplosionParticle.particle(), smallExplosionParticle.data()),
            largeExplosionParticle == null
                ? new DustParticleOptions(0, 0.0F)
                : CraftParticle.createParticleParam(
                largeExplosionParticle.particle(), largeExplosionParticle.data()),
            Level.DEFAULT_EXPLOSION_BLOCK_PARTICLES,
            sound == null ? Holder.direct(SoundEvents.EMPTY) : CraftSound.bukkitToMinecraftHolder(sound),
            serverExplosion -> serverExplosion.excludeSourceFromDamage = excludeSourceFromDamage)
            .wasCanceled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Explosion.Builder {
        private @Nullable Entity source;
        private @Nullable Location center;
        private float radius = 4;
        private boolean breakBlocks = true;
        private boolean setFire = true;
        private @Nullable ParticleBuilder smallExplosionParticle = Particle.EXPLOSION.builder();
        private @Nullable ParticleBuilder largeExplosionParticle = Particle.EXPLOSION_EMITTER.builder();
        private @Nullable Sound sound = Sound.ENTITY_GENERIC_EXPLODE;
        private boolean excludeSourceFromDamage = true;
        private @Nullable DamageCalculator damageCalculator;

        private Builder() {
        }

        public ExplosionImpl build() {
            Preconditions.checkNotNull(center, "Explosion center cannot be null");
            return new ExplosionImpl(
                source,
                center,
                radius,
                breakBlocks,
                setFire,
                smallExplosionParticle,
                largeExplosionParticle,
                sound,
                excludeSourceFromDamage,
                damageCalculator
            );
        }

        public Builder source(Entity source) {
            this.source = source;
            return this;
        }

        public Builder center(Location center) {
            this.center = center;
            return this;
        }

        public Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder breakBlocks(boolean breakBlocks) {
            this.breakBlocks = breakBlocks;
            return this;
        }

        public Builder setFire(boolean setFire) {
            this.setFire = setFire;
            return this;
        }

        public Builder smallExplosionParticle(@Nullable ParticleBuilder smallExplosionParticle) {
            this.smallExplosionParticle = smallExplosionParticle;
            return this;
        }

        public Builder largeExplosionParticle(@Nullable ParticleBuilder largeExplosionParticle) {
            this.largeExplosionParticle = largeExplosionParticle;
            return this;
        }

        public Builder sound(@Nullable Sound sound) {
            this.sound = sound;
            return this;
        }

        public Builder excludeSourceFromDamage(boolean excludeSourceFromDamage) {
            this.excludeSourceFromDamage = excludeSourceFromDamage;
            return this;
        }

        public Builder damageProvider(@Nullable DamageCalculator damageCalculator) {
            this.damageCalculator = damageCalculator;
            return this;
        }
    }
}

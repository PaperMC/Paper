package io.papermc.paper.world.explosion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.jetbrains.annotations.NotNull;

public class PaperExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final Explosion explosion;

    public PaperExplosionDamageCalculator(Explosion explosion) {
        this.explosion = explosion;
    }

    @Override
    public float getEntityDamageAmount(
            net.minecraft.world.level.@NotNull Explosion explosion, @NotNull Entity entity, float seenPercent) {
        var provider = this.explosion.damageCalculator();
        return provider == null ? super.getEntityDamageAmount(explosion, entity, seenPercent) : provider.getDamage(this.explosion, entity.getBukkitEntity(), seenPercent);
    }
}

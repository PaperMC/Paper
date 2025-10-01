package io.papermc.paper.world.explosion;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.jetbrains.annotations.NotNull;

public class PaperExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final Explosion explosion;

    public PaperExplosionDamageCalculator(Explosion explosion) {
        this.explosion = explosion;
    }

    @Override
    public @NotNull Optional<Float> getBlockExplosionResistance(net.minecraft.world.level.@NotNull Explosion explosion, @NotNull BlockGetter reader, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluid) {
        var provider = this.explosion.blockExplosionResistanceProvider();
        return provider == null ? super.getBlockExplosionResistance(explosion, reader, pos, state, fluid) : provider.getBlockExplosionResistance(this.explosion, CraftBlock.at(explosion.level(), pos));
    }

    @Override
    public boolean shouldBlockExplode(net.minecraft.world.level.@NotNull Explosion explosion, @NotNull BlockGetter reader, @NotNull BlockPos pos, @NotNull BlockState state, float power) {
        var filter = this.explosion.blockFilter();
        return filter == null ? super.shouldBlockExplode(explosion, reader, pos, state, power) : filter.shouldExplode(this.explosion, CraftBlock.at(explosion.level(), pos), power);
    }

    @Override
    public boolean shouldDamageEntity(net.minecraft.world.level.@NotNull Explosion explosion, @NotNull Entity entity) {
        var filter = this.explosion.entityFilter();
        return filter == null ? super.shouldDamageEntity(explosion, entity) : filter.shouldDamageEntity(this.explosion, entity.getBukkitEntity());
    }

    @Override
    public float getKnockbackMultiplier(@NotNull Entity entity) {
        var provider = this.explosion.knockbackMultiplierProvider();
        return provider == null ? super.getKnockbackMultiplier(entity) : provider.getKnockbackMultiplier(this.explosion, entity.getBukkitEntity());
    }

    @Override
    public float getEntityDamageAmount(
            net.minecraft.world.level.@NotNull Explosion explosion, @NotNull Entity entity, float seenPercent) {
        var provider = this.explosion.damageProvider();
        return provider == null ? super.getEntityDamageAmount(explosion, entity, seenPercent) : provider.getDamage(this.explosion, entity.getBukkitEntity(), seenPercent);
    }
}

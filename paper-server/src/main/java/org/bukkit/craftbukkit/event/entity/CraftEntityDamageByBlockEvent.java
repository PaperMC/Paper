package org.bukkit.craftbukkit.event.entity;

import java.util.Map;
import java.util.function.Function;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityDamageByBlockEvent extends CraftEntityDamageEvent implements EntityDamageByBlockEvent {

    private final @Nullable Block damager;
    private final @Nullable BlockState damagerState;

    public CraftEntityDamageByBlockEvent(
        final @Nullable Block damager,
        final @Nullable BlockState damagerState,
        final Entity damagee,
        final DamageCause cause,
        final DamageSource damageSource,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions
    ) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
        this.damagerState = damagerState;
    }

    public CraftEntityDamageByBlockEvent(
        final @Nullable Block damager,
        final @Nullable BlockState damagerState,
        final net.minecraft.world.entity.Entity damagee,
        final DamageCause cause,
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final Map<DamageModifier, Double> modifiers,
        final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions
    ) {
        this(damager, damagerState, damagee.getBukkitEntity(), cause, new CraftDamageSource(damageSource), modifiers, modifierFunctions);
    }

    @Override
    public @Nullable Block getDamager() {
        return this.damager;
    }

    @Override
    public @Nullable BlockState getDamagerBlockState() {
        return this.damagerState;
    }
}

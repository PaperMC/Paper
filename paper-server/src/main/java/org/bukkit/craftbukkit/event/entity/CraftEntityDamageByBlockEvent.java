package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Function;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityDamageByBlockEvent extends CraftEntityDamageEvent implements EntityDamageByBlockEvent {

    private final Block damager;
    private final BlockState damagerState;

    public CraftEntityDamageByBlockEvent(final @Nullable Block damager, final @Nullable BlockState damagerState, final Entity damagee, final DamageCause cause, final DamageSource damageSource, final Map<DamageModifier, Double> modifiers, final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
        super(damagee, cause, damageSource, modifiers, modifierFunctions);
        this.damager = damager;
        this.damagerState = damagerState;
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

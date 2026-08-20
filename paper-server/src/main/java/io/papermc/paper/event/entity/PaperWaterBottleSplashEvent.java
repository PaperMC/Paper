package io.papermc.paper.event.entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.event.entity.CraftPotionSplashEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public class PaperWaterBottleSplashEvent extends CraftPotionSplashEvent implements WaterBottleSplashEvent {

    private final Set<LivingEntity> rehydrate;
    private final Set<LivingEntity> extinguish;

    public PaperWaterBottleSplashEvent(
        final ThrownPotion potion,
        final @Nullable Entity hitEntity,
        final @Nullable Block hitBlock,
        final @Nullable BlockFace hitFace,
        final Map<LivingEntity, Double> affectedEntities,
        final Set<LivingEntity> rehydrate,
        final Set<LivingEntity> extinguish
    ) {
        super(potion, hitEntity, hitBlock, hitFace, affectedEntities);
        this.rehydrate = rehydrate;
        this.extinguish = extinguish;
    }

    @Override
    public @Unmodifiable Collection<LivingEntity> getToDamage() {
        return this.affectedEntities.entrySet().stream().filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void doNotDamageAsWaterSensitive(final LivingEntity entity) {
        this.affectedEntities.remove(entity);
    }

    @Override
    public void damageAsWaterSensitive(final LivingEntity entity) {
        this.affectedEntities.put(entity, 1.0);
    }

    @Override
    public Collection<LivingEntity> getToRehydrate() {
        return this.rehydrate;
    }

    @Override
    public Collection<LivingEntity> getToExtinguish() {
        return this.extinguish;
    }
}

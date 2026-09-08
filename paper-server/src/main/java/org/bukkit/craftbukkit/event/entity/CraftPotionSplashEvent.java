package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.phys.HitResult;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jspecify.annotations.Nullable;

public class CraftPotionSplashEvent extends CraftProjectileHitEvent implements PotionSplashEvent {

    protected final Map<LivingEntity, Double> affectedEntities;

    public CraftPotionSplashEvent(final AbstractThrownPotion potion, final @Nullable HitResult hitResult, final Map<LivingEntity, Double> affectedEntities) {
        super(potion, hitResult);
        this.affectedEntities = affectedEntities;
    }

    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion) this.entity;
    }

    @Override
    public Collection<LivingEntity> getAffectedEntities() {
        return new ArrayList<>(this.affectedEntities.keySet());
    }

    @Override
    public double getIntensity(final LivingEntity entity) {
        return this.affectedEntities.getOrDefault(entity, 0.0D);
    }

    @Override
    public void setIntensity(final LivingEntity entity, final double intensity) {
        Preconditions.checkArgument(entity != null, "You must specify a valid entity.");
        if (intensity <= 0.0) {
            this.affectedEntities.remove(entity);
        } else {
            this.affectedEntities.put(entity, Math.min(intensity, 1.0));
        }
    }

    @Override
    public HandlerList getHandlers() {
        return PotionSplashEvent.getHandlerList();
    }
}

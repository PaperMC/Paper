package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import net.minecraft.world.phys.HitResult;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.jspecify.annotations.Nullable;

public class CraftLingeringPotionSplashEvent extends CraftProjectileHitEvent implements LingeringPotionSplashEvent {

    private final AreaEffectCloud effectCloud;
    private boolean allowEmptyCreation;

    public CraftLingeringPotionSplashEvent(final ThrownLingeringPotion potion, final @Nullable HitResult hitResult, final net.minecraft.world.entity.AreaEffectCloud effectCloud) {
        super(potion, hitResult);
        this.effectCloud = (AreaEffectCloud) effectCloud.getBukkitEntity();
    }

    @Override
    public LingeringPotion getEntity() {
        return (LingeringPotion) this.entity;
    }

    @Override
    public AreaEffectCloud getAreaEffectCloud() {
        return this.effectCloud;
    }

    @Override
    public void allowsEmptyCreation(final boolean allowEmptyCreation) {
        this.allowEmptyCreation = allowEmptyCreation;
    }

    @Override
    public boolean allowsEmptyCreation() {
        return this.allowEmptyCreation;
    }

    @Override
    public HandlerList getHandlers() {
        return LingeringPotionSplashEvent.getHandlerList();
    }
}

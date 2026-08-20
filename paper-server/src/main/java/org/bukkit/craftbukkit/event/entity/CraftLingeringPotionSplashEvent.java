package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.jspecify.annotations.Nullable;

public class CraftLingeringPotionSplashEvent extends CraftProjectileHitEvent implements LingeringPotionSplashEvent {

    private final AreaEffectCloud effectCloud;
    private boolean allowEmptyCreation;

    public CraftLingeringPotionSplashEvent(final ThrownPotion potion, final @Nullable Entity hitEntity, final @Nullable Block hitBlock, final @Nullable BlockFace hitFace, final AreaEffectCloud effectCloud) {
        super(potion, hitEntity, hitBlock, hitFace);
        this.effectCloud = effectCloud;
    }

    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion) this.entity;
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

    public static HandlerList getHandlerList() {
        return LingeringPotionSplashEvent.getHandlerList();
    }
}

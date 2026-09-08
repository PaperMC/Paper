package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.phys.HitResult;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ExpBottleEvent;

public class CraftExpBottleEvent extends CraftProjectileHitEvent implements ExpBottleEvent {

    private int experience;
    private boolean showEffect = true;

    public CraftExpBottleEvent(final ThrownExperienceBottle bottle, final HitResult hitResult, final int experience) {
        super(bottle, hitResult);
        this.experience = experience;
    }

    @Override
    public ThrownExpBottle getEntity() {
        return (ThrownExpBottle) this.entity;
    }

    @Override
    public boolean getShowEffect() {
        return this.showEffect;
    }

    @Override
    public void setShowEffect(final boolean showEffect) {
        this.showEffect = showEffect;
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    @Override
    public void setExperience(final int exp) {
        this.experience = exp;
    }

    @Override
    public HandlerList getHandlers() {
        return ExpBottleEvent.getHandlerList();
    }
}

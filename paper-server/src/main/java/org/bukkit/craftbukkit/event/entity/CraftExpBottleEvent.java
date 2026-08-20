package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ExpBottleEvent;
import org.jspecify.annotations.Nullable;

public class CraftExpBottleEvent extends CraftProjectileHitEvent implements ExpBottleEvent {

    private int exp;
    private boolean showEffect = true;

    public CraftExpBottleEvent(final ThrownExpBottle bottle, final @Nullable Entity hitEntity, final @Nullable Block hitBlock, final @Nullable BlockFace hitFace, final int exp) {
        super(bottle, hitEntity, hitBlock, hitFace);
        this.exp = exp;
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
        return this.exp;
    }

    @Override
    public void setExperience(final int exp) {
        this.exp = exp;
    }

    @Override
    public HandlerList getHandlers() {
        return ExpBottleEvent.getHandlerList();
    }
}

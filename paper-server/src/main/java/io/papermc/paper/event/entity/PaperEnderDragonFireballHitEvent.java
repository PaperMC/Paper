package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EnderDragonFireballHitEvent;
import java.util.Collection;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public class PaperEnderDragonFireballHitEvent extends CraftEntityEvent implements EnderDragonFireballHitEvent {

    private final Collection<LivingEntity> targets;
    private final AreaEffectCloud areaEffectCloud;

    private boolean cancelled;

    public PaperEnderDragonFireballHitEvent(final DragonFireball fireball, final Collection<LivingEntity> targets, final AreaEffectCloud areaEffectCloud) {
        super(fireball);
        this.targets = targets;
        this.areaEffectCloud = areaEffectCloud;
    }

    @Override
    public DragonFireball getEntity() {
        return (DragonFireball) this.entity;
    }

    @Override
    public Collection<LivingEntity> getTargets() {
        return this.targets;
    }

    @Override
    public AreaEffectCloud getAreaEffectCloud() {
        return this.areaEffectCloud;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EnderDragonFireballHitEvent.getHandlerList();
    }
}

package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EnderDragonFlameEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.HandlerList;

public class PaperEnderDragonFlameEvent extends CraftEntityEvent implements EnderDragonFlameEvent {

    private final AreaEffectCloud areaEffectCloud;
    private boolean cancelled;

    public PaperEnderDragonFlameEvent(final EnderDragon enderDragon, final AreaEffectCloud areaEffectCloud) {
        super(enderDragon);
        this.areaEffectCloud = areaEffectCloud;
    }

    @Override
    public EnderDragon getEntity() {
        return (EnderDragon) this.entity;
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
        return EnderDragonFlameEvent.getHandlerList();
    }
}

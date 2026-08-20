package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;

public class PaperEntityEffectTickEvent extends CraftEntityEvent implements EntityEffectTickEvent {

    private final PotionEffectType type;
    private final int amplifier;
    private boolean cancelled;

    public PaperEntityEffectTickEvent(final LivingEntity entity, final PotionEffectType type, final int amplifier) {
        super(entity);
        this.type = type;
        this.amplifier = amplifier;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    @Override
    public PotionEffectType getType() {
        return this.type;
    }

    @Override
    public int getAmplifier() {
        return this.amplifier;
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
        return EntityEffectTickEvent.getHandlerList();
    }
}

package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ArrowBodyCountChangeEvent;
import org.checkerframework.checker.index.qual.NonNegative;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public class CraftArrowBodyCountChangeEvent extends CraftEntityEvent implements ArrowBodyCountChangeEvent {

    private final boolean reset;
    private final int oldAmount;
    private int newAmount;

    private boolean cancelled;

    public CraftArrowBodyCountChangeEvent(final LivingEntity entity, final int oldAmount, final int newAmount, final boolean reset) {
        super(entity);
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.reset = reset;
    }

    public CraftArrowBodyCountChangeEvent(final net.minecraft.world.entity.LivingEntity entity, final int oldAmount, final int newAmount, final boolean reset) {
        this(entity.getBukkitEntity(), oldAmount, newAmount, reset);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public boolean isReset() {
        return this.reset;
    }

    @Override
    public @NonNegative int getOldAmount() {
        return this.oldAmount;
    }

    @Override
    public @NonNegative int getNewAmount() {
        return this.newAmount;
    }

    @Override
    public void setNewAmount(final @NonNegative int newAmount) {
        this.newAmount = requireNonNegative(newAmount, "newAmount");
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
        return ArrowBodyCountChangeEvent.getHandlerList();
    }
}

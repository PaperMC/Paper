package io.papermc.paper.event.entity;

import net.minecraft.world.entity.animal.fish.Pufferfish;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.PufferFish;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

public class PaperPufferFishStateChangeEvent extends CraftEntityEvent implements PufferFishStateChangeEvent {

    private final int newPuffState;
    private boolean cancelled;

    public PaperPufferFishStateChangeEvent(final PufferFish entity, final int newPuffState) {
        super(entity);
        this.newPuffState = newPuffState;
    }

    public PaperPufferFishStateChangeEvent(final Pufferfish entity, final int newPuffState) {
        this((PufferFish) entity.getBukkitEntity(), newPuffState);
    }

    @Override
    public PufferFish getEntity() {
        return (PufferFish) this.entity;
    }

    @Override
    public @IntRange(from = Pufferfish.STATE_SMALL, to = Pufferfish.STATE_FULL) int getNewPuffState() {
        return this.newPuffState;
    }

    @Override
    public boolean isInflating() {
        return this.newPuffState > this.getEntity().getPuffState();
    }

    @Override
    public boolean isDeflating() {
        return this.newPuffState < this.getEntity().getPuffState();
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
        return PufferFishStateChangeEvent.getHandlerList();
    }
}

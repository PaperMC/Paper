package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Raider;
import org.bukkit.event.HandlerList;

public class PaperBellRevealRaiderEvent extends CraftBlockEvent implements BellRevealRaiderEvent {

    private final Raider raider;
    private boolean cancelled;

    public PaperBellRevealRaiderEvent(final Block bell, final Raider raider) {
        super(bell);
        this.raider = raider;
    }

    @Override
    public Raider getEntity() {
        return this.raider;
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
        return BellRevealRaiderEvent.getHandlerList();
    }
}

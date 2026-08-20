package io.papermc.paper.event.block;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperTNTPrimeEvent extends CraftBlockEvent implements TNTPrimeEvent {

    private final PrimeReason reason;
    private final @Nullable Entity primerEntity;

    private boolean cancelled;

    public PaperTNTPrimeEvent(final Block block, final PrimeReason reason, final @Nullable Entity primerEntity) {
        super(block);
        this.reason = reason;
        this.primerEntity = primerEntity;
    }

    @Override
    public PrimeReason getReason() {
        return this.reason;
    }

    @Override
    public @Nullable Entity getPrimerEntity() {
        return this.primerEntity;
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
        return TNTPrimeEvent.getHandlerList();
    }
}

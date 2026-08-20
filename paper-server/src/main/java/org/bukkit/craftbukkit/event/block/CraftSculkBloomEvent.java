package org.bukkit.craftbukkit.event.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.SculkSpreader;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SculkBloomEvent;

public class CraftSculkBloomEvent extends CraftBlockEvent implements SculkBloomEvent {

    private int charge;
    private boolean cancelled;

    public CraftSculkBloomEvent(final Block block, final int charge) {
        super(block);
        this.charge = charge;
    }

    @Override
    public int getCharge() {
        return this.charge;
    }

    @Override
    public void setCharge(final int charge) {
        Preconditions.checkArgument(charge >= 0 && charge <= SculkSpreader.MAX_CHARGE, charge + " is not in range [0, %s]", SculkSpreader.MAX_CHARGE);
        this.charge = charge;
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
        return SculkBloomEvent.getHandlerList();
    }
}

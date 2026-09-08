package org.bukkit.craftbukkit.event.block;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SculkSpreader;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SculkBloomEvent;
import org.checkerframework.common.value.qual.IntRange;

import static io.papermc.paper.util.BoundChecker.requireRange;

public class CraftSculkBloomEvent extends CraftBlockEvent implements SculkBloomEvent {

    private int charge;
    private boolean cancelled;

    public CraftSculkBloomEvent(final Block block, final int charge) {
        super(block);
        this.charge = charge;
    }

    public CraftSculkBloomEvent(final Level level, final SculkSpreader.ChargeCursor cursor) {
        this(CraftBlock.at(level, cursor.getPos()), cursor.getCharge());
    }

    @Override
    public @IntRange(from = 0, to = SculkSpreader.MAX_CHARGE) int getCharge() {
        return this.charge;
    }

    @Override
    public void setCharge(final @IntRange(from = 0, to = SculkSpreader.MAX_CHARGE) int charge) {
        this.charge = requireRange(charge, "charge", 0, SculkSpreader.MAX_CHARGE);
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

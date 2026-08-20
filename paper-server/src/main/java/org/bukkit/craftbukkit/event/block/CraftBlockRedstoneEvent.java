package org.bukkit.craftbukkit.event.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.redstone.Redstone;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockRedstoneEvent;

public class CraftBlockRedstoneEvent extends CraftBlockEvent implements BlockRedstoneEvent {

    private final int oldCurrent;
    private int newCurrent;

    public CraftBlockRedstoneEvent(final Block block, final int oldCurrent, final int newCurrent) {
        super(block);
        this.oldCurrent = oldCurrent;
        this.newCurrent = newCurrent;
    }

    @Override
    public int getOldCurrent() {
        return this.oldCurrent;
    }

    @Override
    public int getNewCurrent() {
        return this.newCurrent;
    }

    @Override
    public void setNewCurrent(final int newCurrent) {
        Preconditions.checkArgument(
            newCurrent >= Redstone.SIGNAL_MIN && newCurrent <= Redstone.SIGNAL_MAX,
            "New current must be a redstone signal between %s and %s (was %s)",
            Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX, newCurrent
        );
        this.newCurrent = newCurrent;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockRedstoneEvent.getHandlerList();
    }
}

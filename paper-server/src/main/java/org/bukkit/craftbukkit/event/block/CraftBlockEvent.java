package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.block.BlockEvent;

public abstract class CraftBlockEvent extends CraftEvent implements BlockEvent {

    protected Block block;

    protected CraftBlockEvent(final Block block) {
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }
}

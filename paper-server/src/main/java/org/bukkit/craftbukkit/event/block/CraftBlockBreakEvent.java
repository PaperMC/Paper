package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class CraftBlockBreakEvent extends CraftBlockExpEvent implements BlockBreakEvent {

    private final Player player;
    private boolean dropItems;

    private boolean cancelled;

    public CraftBlockBreakEvent(final Block block, final Player player) {
        super(block, 0);

        this.player = player;
        this.dropItems = true; // Defaults to dropping items as it normally would
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setDropItems(final boolean dropItems) {
        this.dropItems = dropItems;
    }

    @Override
    public boolean isDropItems() {
        return this.dropItems;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}

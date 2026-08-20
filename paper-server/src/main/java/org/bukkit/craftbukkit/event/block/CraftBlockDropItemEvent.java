package org.bukkit.craftbukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDropItemEvent;

public class CraftBlockDropItemEvent extends CraftBlockEvent implements BlockDropItemEvent {

    private final Player player;
    private final BlockState blockState;
    private final List<Item> items;

    private boolean cancelled;

    public CraftBlockDropItemEvent(final Block block, final BlockState blockState, final Player player, final List<Item> items) {
        super(block);
        this.blockState = blockState;
        this.player = player;
        this.items = items;
    }

    @Override
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public List<Item> getItems() {
        return this.items;
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
        return BlockDropItemEvent.getHandlerList();
    }
}

package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CraftBlockDamageEvent extends CraftBlockEvent implements BlockDamageEvent {

    private final Player player;
    private final BlockFace blockFace;
    private final ItemStack itemInHand;
    private boolean instaBreak;

    private boolean cancelled;

    public CraftBlockDamageEvent(final Player player, final Block block, final BlockFace blockFace, final ItemStack itemInHand, final boolean instaBreak) {
        super(block);
        this.player = player;
        this.blockFace = blockFace;
        this.itemInHand = itemInHand;
        this.instaBreak = instaBreak;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public boolean getInstaBreak() {
        return this.instaBreak;
    }

    @Override
    public void setInstaBreak(final boolean instaBreak) {
        this.instaBreak = instaBreak;
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
        return BlockDamageEvent.getHandlerList();
    }
}

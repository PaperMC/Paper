package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftBlockPlaceEvent extends CraftBlockEvent implements BlockPlaceEvent {

    protected Block placedAgainst;
    protected ItemStack itemInHand;
    protected Player player;
    protected BlockState replacedBlockState;
    protected boolean canBuild;
    protected EquipmentSlot hand;

    protected boolean cancelled;

    public CraftBlockPlaceEvent(final Block placedBlock, final BlockState replacedBlockState, final Block placedAgainst, final ItemStack itemInHand, final Player player, final boolean canBuild, final EquipmentSlot hand) {
        super(placedBlock);
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.player = player;
        this.replacedBlockState = replacedBlockState;
        this.canBuild = canBuild;
        this.hand = hand;
    }

    @Override
    public Block getBlockAgainst() {
        return this.placedAgainst;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.itemInHand;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Block getBlockPlaced() {
        return this.block;
    }

    @Override
    public BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }

    @Override
    public boolean canBuild() {
        return this.canBuild;
    }

    @Override
    public void setBuild(final boolean canBuild) {
        this.canBuild = canBuild;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
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
        return BlockPlaceEvent.getHandlerList();
    }
}

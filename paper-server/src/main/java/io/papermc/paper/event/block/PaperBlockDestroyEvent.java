package io.papermc.paper.event.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.event.block.CraftBlockExpEvent;
import org.bukkit.event.HandlerList;

public class PaperBlockDestroyEvent extends CraftBlockExpEvent implements BlockDestroyEvent {

    private final BlockData newState;
    private boolean willDrop;
    private boolean playEffect = true;
    private BlockData effectBlock;

    private boolean cancelled;

    public PaperBlockDestroyEvent(final Block block, final BlockData newState, final BlockData effectBlock, final int xp, final boolean willDrop) {
        super(block, xp);
        this.newState = newState;
        this.effectBlock = effectBlock;
        this.willDrop = willDrop;
    }

    @Override
    public BlockData getEffectBlock() {
        return this.effectBlock;
    }

    @Override
    public void setEffectBlock(final BlockData effectBlock) {
        this.effectBlock = effectBlock.clone();
    }

    @Override
    public BlockData getNewState() {
        return this.newState.clone();
    }

    @Override
    public boolean willDrop() {
        return this.willDrop;
    }

    @Override
    public void setWillDrop(final boolean willDrop) {
        this.willDrop = willDrop;
    }

    @Override
    public boolean playEffect() {
        return this.playEffect;
    }

    @Override
    public void setPlayEffect(final boolean playEffect) {
        this.playEffect = playEffect;
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
        return BlockDestroyEvent.getHandlerList();
    }
}

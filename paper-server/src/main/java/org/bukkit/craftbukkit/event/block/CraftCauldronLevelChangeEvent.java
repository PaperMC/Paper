package org.bukkit.craftbukkit.event.block;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.jspecify.annotations.Nullable;

public class CraftCauldronLevelChangeEvent extends CraftBlockEvent implements CauldronLevelChangeEvent {

    private final @Nullable Entity entity;
    private final ChangeReason reason;
    private final BlockState newState;

    private boolean cancelled;

    public CraftCauldronLevelChangeEvent(final Block block, final @Nullable Entity entity, final ChangeReason reason, final BlockState newBlock) {
        super(block);
        this.entity = entity;
        this.reason = reason;
        this.newState = newBlock;
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    @Override
    public ChangeReason getReason() {
        return this.reason;
    }

    @Override
    public BlockState getNewState() {
        return this.newState;
    }

    @Override
    public int getOldLevel() {
        BlockData oldBlock = this.getBlock().getBlockData();
        return (oldBlock instanceof Levelled levelled) ? levelled.getLevel() : ((oldBlock.getMaterial() == Material.CAULDRON) ? 0 : LayeredCauldronBlock.MAX_FILL_LEVEL);
    }

    @Override
    public int getNewLevel() {
        BlockData newBlock = this.newState.getBlockData();
        return (newBlock instanceof Levelled levelled) ? levelled.getLevel() : ((newBlock.getMaterial() == Material.CAULDRON) ? 0 : LayeredCauldronBlock.MAX_FILL_LEVEL);
    }

    @Override
    public void setNewLevel(final int newLevel) {
        Preconditions.checkArgument(
            0 <= newLevel && newLevel <= LayeredCauldronBlock.MAX_FILL_LEVEL,
            "Cauldron level out of bounds 0 <= %s <= %s",
            newLevel, LayeredCauldronBlock.MAX_FILL_LEVEL
        );
        if (newLevel == 0) {
            this.newState.setType(Material.CAULDRON);
        } else if (this.newState.getBlockData() instanceof Levelled levelled) {
            levelled.setLevel(newLevel);
        } else {
            // Error, non-levellable block
        }
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
        return CauldronLevelChangeEvent.getHandlerList();
    }
}

package org.bukkit.craftbukkit.event.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityInteractEvent;

public class CraftEntityInteractEvent extends CraftEntityEvent implements EntityInteractEvent {

    protected final Block block;
    private boolean cancelled;

    public CraftEntityInteractEvent(final Entity entity, final Block block) {
        super(entity);
        this.block = block;
    }

    public CraftEntityInteractEvent(final net.minecraft.world.entity.Entity entity, final Level level, final BlockPos pos) {
        this(entity.getBukkitEntity(), CraftBlock.at(level, pos));
    }

    @Override
    public Block getBlock() {
        return this.block;
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
        return EntityInteractEvent.getHandlerList();
    }
}

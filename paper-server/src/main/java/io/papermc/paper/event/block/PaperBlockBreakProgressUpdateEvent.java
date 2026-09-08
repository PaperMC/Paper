package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperBlockBreakProgressUpdateEvent extends CraftBlockEvent implements BlockBreakProgressUpdateEvent {

    private final float progress;
    private final Entity entity;

    public PaperBlockBreakProgressUpdateEvent(final Block block, final float progress, final Entity entity) {
        super(block);
        this.progress = progress;
        this.entity = entity;
    }

    public PaperBlockBreakProgressUpdateEvent(final Level level, final BlockPos pos, final int progress, final net.minecraft.world.entity.Entity entity) {
        this(CraftBlock.at(level, pos), Mth.clamp(progress, 0, 10) / 10.0F, entity.getBukkitEntity());
    }

    @Override
    public float getProgress() {
        return this.progress;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockBreakProgressUpdateEvent.getHandlerList();
    }
}

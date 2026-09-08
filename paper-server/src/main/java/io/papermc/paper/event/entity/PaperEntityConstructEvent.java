package io.papermc.paper.event.entity;

import java.util.Collections;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

public class PaperEntityConstructEvent extends CraftEntityEvent implements EntityConstructEvent {

    private final List<Block> blocks;
    private boolean cancelled;

    public PaperEntityConstructEvent(final Entity entity, final List<Block> blocks) {
        super(entity);
        this.blocks = Collections.unmodifiableList(blocks);
    }

    public PaperEntityConstructEvent(final net.minecraft.world.entity.Entity entity, final Level level, final BlockPattern.BlockPatternMatch match) {
        this(entity.getBukkitEntity(), CraftBlock.getMatchingBlocks(level, match));
    }

    @Override
    public @Unmodifiable List<Block> getBlocks() {
        return this.blocks;
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
        return EntityConstructEvent.getHandlerList();
    }
}

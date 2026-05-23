package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Golem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import java.util.List;

/**
 * Called just before a {@link Golem} spawns due to a pattern of blocks being constructed.
 */
@NullMarked
public class GolemConstructEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<Block> blocks;
    private boolean cancelled;

    @ApiStatus.Internal
    public GolemConstructEvent(Golem golem, List<Block> blocks) {
        super(golem);
        this.blocks = List.copyOf(blocks);
    }

    @Override
    public Golem getEntity() {
        return (Golem) super.getEntity();
    }

    /**
     * Get an immutable list of the blocks for this golem
     *
     * @return the golem blocks
     */
    public @Unmodifiable List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

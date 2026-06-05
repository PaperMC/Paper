package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import java.util.List;

/**
 * Called just before an {@link Entity} spawns due to a pattern of blocks being constructed (golems, the wither, etc.)
 * <br>
 * Note: This event is fired before {@link EntitySpawnEvent}, before the entity is added to the world,
 * the success of this event does not guarantee the entity will actually spawn.
 */
@NullMarked
public class EntityConstructEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<Block> blocks;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityConstructEvent(Entity entity, List<Block> blocks) {
        super(entity);
        this.blocks = List.copyOf(blocks);
    }

    /**
     * Get an immutable list of the blocks used in this construction
     * <br>
     * Note: This includes any required air blocks
     * @return the blocks
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

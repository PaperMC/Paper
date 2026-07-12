package io.papermc.paper.event.server;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * This event is emitted by the server when the data fixer is removing a block entity associated with a block
 * due to the block entity type no longer existing in this minecraft version.
 * <p>
 * Notable examples include the removal of the bed block entity in 26.2.
 * <p>
 * <b>WARNING</b>: While this event is prefixed with 'Async' it is executed as part of the chunk loading process.
 * It may hence either execute on the chunk loading worker threads or the servers main thread, if a chunk load
 * is executed there. In either case, heavy or blocking work is <b>strongly</b> discouraged to ensure the
 * server runs smoothly. Schedule large amount of work into separate thread pools.
 */
@NullMarked
public class AsyncServerDataFixerRemoveBlockEntityEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Key worldKey;
    private final Key blockEntityType;
    private final BlockPosition blockPosition;
    private final PersistentDataContainerView persistentDataContainerView;

    @ApiStatus.Internal
    public AsyncServerDataFixerRemoveBlockEntityEvent(
        final Key worldKey,
        final Key blockEntityType,
        final BlockPosition blockPosition,
        final PersistentDataContainerView persistentDataContainerView
    ) {
        super(!Bukkit.isPrimaryThread());
        this.worldKey = worldKey;
        this.blockEntityType = blockEntityType;
        this.blockPosition = blockPosition;
        this.persistentDataContainerView = persistentDataContainerView;
    }

    /**
     * {@return the key representing the no longer existing block entity type}
     */
    public Key getBlockEntityType() {
        return blockEntityType;
    }

    /**
     * {@return the key of the world this block entity was removed from}
     *
     * @see org.bukkit.Server#getWorld(Key)
     */
    public Key getWorldKey() {
        return worldKey;
    }

    /**
     * {@return the position of the block entity that was removed}
     */
    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    /**
     * {@return an immutable view of the persistent data container that was attached to the removed block entity}
     */
    public PersistentDataContainerView getPersistentDataContainerView() {
        return persistentDataContainerView;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

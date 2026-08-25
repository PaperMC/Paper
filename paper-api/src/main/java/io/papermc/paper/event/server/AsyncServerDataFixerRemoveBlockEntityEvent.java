package io.papermc.paper.event.server;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.key.Key;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is emitted by the server when the data fixer is removing a block entity associated with a block
 * due to the block entity type no longer existing in this minecraft version.
 * <p>
 * Notable examples include the removal of the bed block entity in 26.2.
 * <p>
 * <b>WARNING</b>: While this event is prefixed with 'Async' it is executed as part of the chunk loading process.
 * It may hence either execute on the chunk loading worker threads or the servers main thread, if a chunk load
 * is executed there. In either case, heavy or blocking work is <b>strongly</b> discouraged to ensure the
 * server runs smoothly. This is also applicable for the chunk loading worker threads, which the server main thread
 * might be blocking on.
 * Schedule large amount of work into separate thread pools.
 */
public interface AsyncServerDataFixerRemoveBlockEntityEvent extends Event {

    /**
     * {@return the key of the world this block entity was removed from}
     *
     * @see org.bukkit.Server#getWorld(Key)
     */
    Key getWorldKey();

    /**
     * {@return the key representing the no longer existing block entity type}
     */
    Key getBlockEntityType();

    /**
     * {@return the position of the block entity that was removed}
     */
    BlockPosition getBlockPosition();

    /**
     * {@return an immutable view of the persistent data container that was attached to the removed block entity}
     */
    PersistentDataContainerView getPersistentDataContainerView();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

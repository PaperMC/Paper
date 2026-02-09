package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a block physics check is called.
 * <p>
 * This event is a high frequency event, it may be called thousands of times per
 * a second on a busy server. Plugins are advised to listen to the event with
 * caution and only perform lightweight checks when using it.
 * <p>
 * In addition to this, cancelling the event is liable to leave the world in an
 * inconsistent state. For example if you use the event to leave a block
 * floating in mid air when that block has a requirement to be attached to
 * something, there is no guarantee that the floating block will persist across
 * server restarts or map upgrades.
 * <p>
 * Plugins should also note that where possible this event may only called for
 * the "root" block of physics updates in order to limit event spam. Physics
 * updates that cause other blocks to change their state may not result in an
 * event for each of those blocks (usually adjacent). If you are concerned about
 * monitoring these changes then you should check adjacent blocks yourself.
 */
public interface BlockPhysicsEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the source block that triggered this event.
     *
     * @return The source block
     * @apiNote This will default to block if not set.
     */
    Block getSourceBlock();

    /**
     * Gets the type of block that changed, causing this event.
     * This is the type of {@link #getBlock()} at the time of the event.
     *
     * @return Changed block's type
     */
    Material getChangedType();

    /**
     * Gets the BlockData of the block that changed, causing this event.
     * This is the BlockData of {@link #getBlock()} at the time of the event.
     *
     * @return Changed block's BlockData
     */
    BlockData getChangedBlockData();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

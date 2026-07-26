package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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
public class BlockPhysicsEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockData changed;
    private final Block sourceBlock;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public BlockPhysicsEvent(final Block block, final BlockData changed, final int sourceX, final int sourceY, final int sourceZ) {
        this(block, changed, block.getWorld().getBlockAt(sourceX, sourceY, sourceZ));
    }

    @ApiStatus.Internal
    public BlockPhysicsEvent(@NotNull final Block block, @NotNull final BlockData changed) {
        this(block, changed, block);
    }

    @ApiStatus.Internal
    public BlockPhysicsEvent(@NotNull final Block block, @NotNull final BlockData changed, @NotNull final Block sourceBlock) {
        super(block);
        this.changed = changed;
        this.sourceBlock = sourceBlock;
    }

    /**
     * Gets the source block that triggered this event.
     *
     * @return The source block
     * @apiNote This will default to block if not set.
     */
    @NotNull
    public Block getSourceBlock() {
        return this.sourceBlock;
    }

    /**
     * Gets the type of block that changed, causing this event.
     * This is the type of {@link #getBlock()} at the time of the event.
     *
     * @return Changed block's type
     */
    @NotNull
    public Material getChangedType() {
        return this.changed.getMaterial();
    }

    /**
     * Gets the BlockData of the block that changed, causing this event.
     * This is the BlockData of {@link #getBlock()} at the time of the event.
     *
     * @return Changed block's BlockData
     */
    @NotNull
    public BlockData getChangedBlockData() {
        return this.changed.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

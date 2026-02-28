package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * When an item causes the spawning of an entity. Most event fires are going to
 * be the through the sub-event {@link org.bukkit.event.entity.EntityPlaceEvent} but this
 * event will also be fired for mob spawn eggs from players and dispensers.
 */
@NullMarked
public class ItemSpawnEntityEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable Player player;
    private final Block block;
    private final BlockFace blockFace;
    private final ItemStack spawningStack;
    private boolean cancelled;

    @ApiStatus.Internal
    public ItemSpawnEntityEvent(final Entity entity, final @Nullable Player player, final Block block, final BlockFace blockFace, final ItemStack spawningStack) {
        super(entity);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
        this.spawningStack = spawningStack;
    }

    /**
     * Returns the player placing the entity (if one is available).
     *
     * @return the player placing the entity
     */
    public @Nullable Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the block that the entity was placed on
     *
     * @return the block that the entity was placed on
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * Returns the face of the block that the entity was placed on
     *
     * @return the face of the block that the entity was placed on
     */
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    /**
     * Gets the item responsible for spawning the entity. Mutating
     * this item has no effect.
     * <p>
     * May return an empty item if the actual stack isn't available.
     *
     * @return the spawning item
     */
    public ItemStack getSpawningStack() {
        return this.spawningStack;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

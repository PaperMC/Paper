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
 * Triggered when an entity is created in the world by "placing" an item
 * on a block from a player or dispenser.
 * <br>
 * Note that this event is currently only fired for these specific placements:
 * armor stands, boats, minecarts, end crystals, mob buckets, spawn eggs and tnt (dispenser only).
 *
 * @see org.bukkit.event.hanging.HangingPlaceEvent for paintings, item frames, and leashes.
 * @see org.bukkit.event.entity.EntityPlaceEvent for a player-only version with more context
 * @see BlockPlaceEntityEvent for a dispenser-only version with more context
 */
@NullMarked
public abstract class PlaceEntityEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable Player player;
    private final Block block;
    private final BlockFace blockFace;
    private final ItemStack spawningItem;
    private boolean cancelled;

    protected PlaceEntityEvent(final Entity entity, final @Nullable Player player, final Block block, final BlockFace blockFace, final ItemStack spawningItem) {
        super(entity);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
        this.spawningItem = spawningItem;
    }

    /**
     * {@return the player placing the entity (if available)}
     */
    public @Nullable Player getPlayer() {
        return this.player;
    }

    /**
     * {@return the block that the entity was placed on}
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * {@return the face of the block that the entity was placed on}
     */
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    /**
     * Gets the item responsible for spawning the entity. Mutating
     * this item has no effect.
     * <p>
     * May return an empty item if not available.
     *
     * @return the spawning item
     */
    public ItemStack getSpawningItem() {
        return this.spawningItem.clone();
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

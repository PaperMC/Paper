package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Triggered when an entity is created in the world by a player "placing" an item
 * on a block.
 * <br>
 * Note that this event is currently only fired for four specific placements:
 * armor stands, boats, minecarts, and end crystals.
 */
public class EntityPlaceEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final Block block;
    private final BlockFace blockFace;
    private final EquipmentSlot hand;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityPlaceEvent(@NotNull final Entity entity, @Nullable final Player player, @NotNull final Block block, @NotNull final BlockFace blockFace, @NotNull final EquipmentSlot hand) {
        super(entity);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
        this.hand = hand;
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.19.2", forRemoval = true)
    public EntityPlaceEvent(@NotNull final Entity entity, @Nullable final Player player, @NotNull final Block block, @NotNull final BlockFace blockFace) {
        this(entity, player, block, blockFace, EquipmentSlot.HAND);
    }

    /**
     * Returns the player placing the entity
     *
     * @return the player placing the entity
     */
    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the block that the entity was placed on
     *
     * @return the block that the entity was placed on
     */
    @NotNull
    public Block getBlock() {
        return this.block;
    }

    /**
     * Returns the face of the block that the entity was placed on
     *
     * @return the face of the block that the entity was placed on
     */
    @NotNull
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    /**
     * Get the hand used to place the entity.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return this.hand;
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

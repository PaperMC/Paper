package org.bukkit.event.entity;

import io.papermc.paper.event.entity.PlaceEntityEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Triggered when an entity is created in the world by a player "placing" an item
 * on a block.
 * <br>
 * Note that this event is currently only fired for these specific placements:
 * armor stands, boats, minecarts, end crystals, and mob buckets.
 *
 * @see org.bukkit.event.hanging.HangingPlaceEvent for paintings, item frames, and leashes.
 * @see io.papermc.paper.event.entity.BlockPlaceEntityEvent for a dispenser-only version
 * @see io.papermc.paper.event.entity.PlaceEntityEvent to listen to both blocks and players placing entities
 */
@NullMarked
public class EntityPlaceEvent extends PlaceEntityEvent {

    private final EquipmentSlot hand;

    @ApiStatus.Internal
    public EntityPlaceEvent(
        final Entity entity,
        final @Nullable Player player,
        final Block block,
        final BlockFace blockFace,
        final EquipmentSlot hand,
        final ItemStack spawningStack
    ) {
        super(entity, player, block, blockFace, spawningStack);
        this.hand = hand;
    }

    @Override
    public @Nullable Player getPlayer() {
        return super.getPlayer();
    }

    @Override
    public Block getBlock() {
        return super.getBlock();
    }

    @Override
    public BlockFace getBlockFace() {
        return super.getBlockFace();
    }

    /**
     * Get the hand used to place the entity.
     *
     * @return the hand
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }

    @Override
    public void setCancelled(final boolean cancel) {
        super.setCancelled(cancel);
    }
}

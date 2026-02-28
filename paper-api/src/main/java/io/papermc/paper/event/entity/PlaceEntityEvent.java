package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Triggered when an entity is created in the world by "placing" an item
 * on a block from a player or dispenser.
 * <br>
 * Note that this event is currently only fired for these specific placements:
 * armor stands, boats, minecarts, end crystals, mob buckets, and tnt (dispenser only).
 * @see org.bukkit.event.hanging.HangingPlaceEvent for paintings, item frames, and leashes.
 * @see org.bukkit.event.entity.EntityPlaceEvent for a player-only version with more context
 * @see BlockPlaceEntityEvent for a dispener-only version with more context
 */
@NullMarked
public abstract class PlaceEntityEvent extends ItemSpawnEntityEvent {

    @ApiStatus.Internal
    protected PlaceEntityEvent(final Entity entity, final @Nullable Player player, final Block block, final BlockFace blockFace, final ItemStack spawningStack) {
        super(entity, player, block, blockFace, spawningStack);
    }
}

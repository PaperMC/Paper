package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block, like a dispenser, places an
 * entity. {@link #getPlayer()} will always be {@code null}.
 *
 * @see org.bukkit.event.hanging.HangingPlaceEvent for paintings, item frames, and leashes.
 * @see org.bukkit.event.entity.EntityPlaceEvent for a player-only version with more context
 * @see PlaceEntityEvent to listen to both blocks and players placing entities
 */
@NullMarked
public class BlockPlaceEntityEvent extends PlaceEntityEvent {

    private final Dispenser dispenser;

    @ApiStatus.Internal
    public BlockPlaceEntityEvent(final Entity entity, final Block block, final BlockFace blockFace, final ItemStack spawningStack, final Dispenser dispenser) {
        super(entity, null, block, blockFace, spawningStack);
        this.dispenser = dispenser;
    }

    /**
     * Get the dispenser responsible for placing the entity.
     *
     * @return a non-snapshot Dispenser
     */
    public Dispenser getDispenser() {
        return this.dispenser;
    }

    /**
     * Player will always be null on this event.
     */
    @Override
    @Contract("-> null")
    public @Nullable Player getPlayer() {
        return null;
    }
}

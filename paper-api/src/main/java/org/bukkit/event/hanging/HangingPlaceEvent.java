package org.bukkit.event.hanging;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Triggered when a hanging entity is created in the world
 */
public class HangingPlaceEvent extends HangingEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final Block block;
    private final BlockFace blockFace;

    public HangingPlaceEvent(@NotNull final Hanging hanging, @Nullable final Player player, @NotNull final Block block, @NotNull final BlockFace blockFace) {
        super(hanging);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
    }

    /**
     * Returns the player placing the hanging entity
     *
     * @return the player placing the hanging entity
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the block that the hanging entity was placed on
     *
     * @return the block that the hanging entity was placed on
     */
    @NotNull
    public Block getBlock() {
        return block;
    }

    /**
     * Returns the face of the block that the hanging entity was placed on
     *
     * @return the face of the block that the hanging entity was placed on
     */
    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

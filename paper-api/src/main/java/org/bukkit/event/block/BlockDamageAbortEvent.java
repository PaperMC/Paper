package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player stops damaging a Block.
 * @see BlockDamageEvent
 */
public class BlockDamageAbortEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack itemstack;

    public BlockDamageAbortEvent(@NotNull final Player player, @NotNull final Block block, @NotNull final ItemStack itemInHand) {
        super(block);
        this.player = player;
        this.itemstack = itemInHand;
    }

    /**
     * Gets the player that stopped damaging the block involved in this event.
     *
     * @return The player that stopped damaging the block
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the ItemStack for the item currently in the player's hand.
     *
     * @return The ItemStack for the item currently in the player's hand
     */
    @NotNull
    public ItemStack getItemInHand() {
        return itemstack;
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

package io.papermc.paper.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player uses shears on a block.
 * <p>
 * This event is <b>not</b> called when a player breaks blocks with shears, but rather when a
 * player uses the shears on a block to collect drops from it and/or modify its state.
 * <p>
 * Examples include shearing a pumpkin to turn it into a carved pumpkin or shearing a beehive to get honeycomb.
 */
@NullMarked
public class PlayerShearBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private final List<ItemStack> drops;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerShearBlockEvent(final Player player, final Block block, final ItemStack item, final EquipmentSlot hand, final List<ItemStack> drops) {
        super(player);
        this.block = block;
        this.item = item;
        this.hand = hand;
        this.drops = drops;
    }

    /**
     * Gets the block being sheared in this event.
     *
     * @return The {@link Block} which block is being sheared in this event.
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * Gets the item used to shear the block.
     *
     * @return The {@link ItemStack} of the shears.
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets the hand used to shear the block.
     *
     * @return Either {@link EquipmentSlot#HAND} OR {@link EquipmentSlot#OFF_HAND}.
     */
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Gets the resulting drops of this event.
     *
     * @return A mutable {@link List list} of {@link ItemStack items} that will be dropped as result of this event.
     */
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Gets whether the shearing of the block should be cancelled or not.
     *
     * @return Whether the shearing of the block should be cancelled or not.
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets whether the shearing of the block should be cancelled or not.
     *
     * @param cancel whether the shearing of the block should be cancelled or not.
     */
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

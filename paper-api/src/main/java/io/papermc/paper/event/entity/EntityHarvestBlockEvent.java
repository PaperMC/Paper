package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.util.List;

/**
 * This event is called whenever an entity harvests a block.
 * <br>
 * For the player case please use {@link org.bukkit.event.player.PlayerHarvestBlockEvent}
 * <br>
 * A 'harvest' is when a block drops an item (usually some sort of crop) and
 * changes state, but is not broken in order to drop the item.
 * <br>
 * This event is not called for when a block is broken, to handle that, listen
 * for {@link org.bukkit.event.block.BlockBreakEvent} and
 * {@link org.bukkit.event.block.BlockDropItemEvent}.
 */
@NullMarked
public class EntityHarvestBlockEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancel = false;
    private final Block harvestedBlock;
    private final List<ItemStack> itemsHarvested;

    @ApiStatus.Internal
    public EntityHarvestBlockEvent(final Entity entity, final Block harvestedBlock, final List<ItemStack> itemsHarvested) {
        super(entity);
        this.harvestedBlock = harvestedBlock;
        this.itemsHarvested = itemsHarvested;
    }

    /**
     * Gets the block that is being harvested.
     *
     * @return The block that is being harvested
     */
    public Block getHarvestedBlock() {
        return harvestedBlock;
    }

    /**
     * Gets a list of items that are being harvested from this block.
     *
     * @return A mutable list of items that are being harvested from this block
     * @apiNote {@link org.bukkit.entity.Fox} has a behavior where, if it does not have
     * an item in its mouth ({@link org.bukkit.inventory.EquipmentSlot#HAND}), it will
     * take one unit from the first ItemStack in this harvest and put it there.
     */
    public List<ItemStack> getItemsHarvested() {
        return itemsHarvested;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

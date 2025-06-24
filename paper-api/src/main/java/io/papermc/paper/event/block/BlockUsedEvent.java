package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called after a block has been interacted with by a player.
 * <p>
 * This event is fired after all internal logic and result determination
 * has completed, including any overrides set in {@link BlockPreUseEvent}.
 * It represents the finalized outcome of the block interaction.
 * <p>
 * This event is not cancellable and is primarily used for observing
 * or reacting to completed interactions.
 *
 * @see BlockPreUseEvent
 * @see BlockUseEvent.Result
 */
@NullMarked
public class BlockUsedEvent extends BlockUseEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockUseEvent.Result result;

    @ApiStatus.Internal
    public BlockUsedEvent(final Block block, final Player player, final @Nullable ItemStack item, final EquipmentSlot hand, final BlockUseEvent.Result resultOverride) {
        super(block, player, item, hand);
        this.result = resultOverride;
    }

    /**
     * Gets the result for this interaction.
     * <p>
     * This result reflects what the outcome of the interaction was,
     * and can be used to determine if the use was handled, passed through, etc.
     *
     * @return the result
     */
    public BlockUseEvent.Result getResult() {
        return result;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}

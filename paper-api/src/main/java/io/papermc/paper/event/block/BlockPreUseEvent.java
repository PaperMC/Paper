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
 * Called before a block is used by a player, allowing plugins to override or alter interaction behavior.
 * <p>
 * This event occurs prior to any vanilla interaction logic. It allows for intercepting
 * the interaction attempt and optionally specifying a {@link BlockUseEvent.Result} override that will
 * replace standard behavior.
 * <p>
 * This event is useful for controlling or blocking interactions entirely, customizing behavior,
 * or injecting logic before the result of the block use is finalized.
 *
 * @see BlockUsedEvent
 * @see BlockUseEvent.Result
 */
@NullMarked
public class BlockPreUseEvent extends BlockUseEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private BlockUseEvent.@Nullable Result resultOverride;

    @ApiStatus.Internal
    public BlockPreUseEvent(final Block block, final Player player, final @Nullable ItemStack item, final EquipmentSlot hand) {
        super(block, player, item, hand);
    }

    /**
     * Gets the result override for this interaction.
     * <p>
     * If a non-null result is set, the server will bypass
     * vanilla interaction logic and use this result instead.
     *
     * @return the result override, or {@code null} if not overridden
     */
    public BlockUseEvent.@Nullable Result getResultOverride() {
        return resultOverride;
    }

    /**
     * Sets a result override for this interaction.
     * <p>
     * When set, vanilla behavior for this block interaction is skipped
     * in favor of the provided {@link BlockUseEvent.Result}.
     *
     * @param resultOverride the result to override interaction logic with, or {@code null} to clear
     */
    public void setResultOverride(final BlockUseEvent.@Nullable Result resultOverride) {
        this.resultOverride = resultOverride;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}

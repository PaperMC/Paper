package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called before a block is used by a player, allowing plugins to override or alter interaction behavior.
 * <p>
 * This event occurs prior to any vanilla interaction logic. It allows for intercepting
 * the interaction attempt and optionally specifying a {@link BlockUseResult} override that will
 * replace standard behavior.
 * <p>
 * This event is useful for controlling or blocking interactions entirely, customizing behavior,
 * or injecting logic before the result of the block use is finalized.
 *
 * @see BlockUsedEvent
 * @see BlockUseResult
 */
@NullMarked
public class BlockPreUseEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final @Nullable ItemStack item;
    private final EquipmentSlot hand;
    private @Nullable BlockUseResult resultOverride;

    @ApiStatus.Internal
    public BlockPreUseEvent(final Block block, final Player player, final @Nullable ItemStack item, final EquipmentSlot hand) {
        super(block);
        this.player = player;
        this.item = item;
        this.hand = hand;
    }

    /**
     * Gets the player interacting with the block.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the item used to interact with the block,
     * or {@code null} if no item was used.
     *
     * @return the item used in the interaction, or {@code null}
     */
    public @Nullable ItemStack getItem() {
        return item;
    }

    /**
     * Gets the hand used to interact with the block.
     *
     * @return the hand
     */
    public EquipmentSlot getHand() {
        return hand;
    }

    /**
     * Gets the result override for this interaction.
     * <p>
     * If a non-null result is set, the server will bypass
     * vanilla interaction logic and use this result instead.
     *
     * @return the result override, or {@code null} if not overridden
     */
    public @Nullable BlockUseResult getResultOverride() {
        return resultOverride;
    }

    /**
     * Sets a result override for this interaction.
     * <p>
     * When set, vanilla behavior for this block interaction is skipped
     * in favor of the provided {@link BlockUseResult}.
     *
     * @param resultOverride the result to override interaction logic with, or {@code null} to clear
     */
    public void setResultOverride(final @Nullable BlockUseResult resultOverride) {
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

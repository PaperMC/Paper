package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Base class for events related to a player interacting with a block.
 * <p>
 * This event encapsulates common data for block interaction scenarios,
 * such as the player involved, the item used, and the hand used for the action.
 * <p>
 * This class is not fired directly. Use {@link BlockPreUseEvent} or {@link BlockUsedEvent}
 * for actual interaction events.
 *
 * @see BlockPreUseEvent
 * @see BlockUsedEvent
 * @see BlockUseEvent.Result
 */
@NullMarked
public abstract class BlockUseEvent extends BlockEvent {

    private final Player player;
    private final @Nullable ItemStack item;
    private final EquipmentSlot hand;

    protected BlockUseEvent(final Block block, final Player player, final @Nullable ItemStack item, final EquipmentSlot hand) {
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
     * Represents the outcome of a block use interaction.
     */
    public enum Result {

        /**
         * The block has successfully handled the interaction.
         * No further logic (e.g., item use) will occur.
         */
        SUCCESS,

        /**
         * The block did not handle the interaction.
         * Interaction should proceed with post-block logic,
         * such as using the item in hand (e.g., placing a block).
         */
        PASS,

        /**
         * The interaction was blocked or failed.
         */
        FAIL,

        /**
         * The interaction should be reattempted with an empty hand.
         */
        TRY_WITH_EMPTY_HAND

    }
}

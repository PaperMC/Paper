package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Represents an event that is called when a player interacts with an object or
 * air, potentially fired once for each hand. The hand can be determined using
 * {@link #getHand()}.
 * <p>
 * This event will fire as cancelled if the vanilla behavior is to do nothing
 * (e.g. interacting with air). For the purpose of avoiding doubt, this means
 * that the event will only be in the cancelled state if it is fired as a result
 * of some prediction made by the server where no subsequent code will run,
 * rather than when the subsequent interaction activity (e.g. placing a block in
 * an illegal position ({@link BlockCanBuildEvent}) will fail).
 */
public interface PlayerInteractEvent extends PlayerEvent, Cancellable {

    /**
     * Returns the action type
     *
     * @return Action returns the type of interaction
     */
    Action getAction();

    /**
     * Gets the cancellation state of this event. Set to {@code true} if you want to
     * prevent buckets from placing water and so forth
     *
     * @return boolean cancellation state
     * @deprecated This event has two possible cancellation states, one for
     * {@link #useInteractedBlock()} and one for {@link #useItemInHand()}. It is
     * possible a call might have the former false, but the latter {@code true}, e.g. in
     * the case of using a firework whilst gliding. Callers should check the
     * relevant methods individually.
     */
    @Override
    @Deprecated(since = "1.14")
    boolean isCancelled();

    /**
     * {@inheritDoc}
     * <p>
     * Canceling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     */
    @Override
    void setCancelled(boolean cancel);

    /**
     * Returns the item in hand represented by this event
     *
     * @return ItemStack the item used
     */
    @Nullable ItemStack getItem();

    /**
     * Convenience method. Returns the material of the item represented by
     * this event
     *
     * @return Material the material of the item used
     */
    Material getMaterial();

    /**
     * Check if this event involved a block
     *
     * @return boolean {@code true} if it did
     */
    boolean hasBlock();

    /**
     * Check if this event involved an item
     *
     * @return boolean {@code true} if it did
     */
    boolean hasItem();

    /**
     * Convenience method to inform the user whether this was a block
     * placement event.
     *
     * @return boolean {@code true} if the item in hand was a block
     */
    boolean isBlockInHand();

    /**
     * Returns the clicked block
     *
     * @return Block returns the block clicked with this item.
     */
    @Nullable Block getClickedBlock();

    /**
     * Returns the face of the block that was clicked
     *
     * @return BlockFace returns the face of the block that was clicked
     */
    BlockFace getBlockFace();

    /**
     * The hand used to perform this interaction. May be {@code null} in the case of
     * {@link Action#PHYSICAL}.
     *
     * @return the hand used to interact. May be {@code null}.
     */
    @Nullable EquipmentSlot getHand();

    /**
     * Gets the exact position on the block the player interacted with, this will
     * be {@code null} outside of {@link Action#RIGHT_CLICK_BLOCK}.
     * <p>
     * All vector components are between 0.0 and 1.0 inclusive.
     *
     * @deprecated misleading, use {@link #getInteractionPoint()}
     * @return the clicked position. May be {@code null}.
     */
    @Deprecated
    @Nullable Vector getClickedPosition();

    /**
     * The exact point at which the interaction occurred. May be {@code null}.
     *
     * @return the exact interaction point. May be {@code null}.
     */
    @Nullable Location getInteractionPoint();

    /**
     * This controls the action to take with the block (if any) that was
     * clicked on. This event gets processed for all blocks, but most don't
     * have a default action
     *
     * @return the action to take with the interacted block
     */
    Result useInteractedBlock();

    /**
     * @param useInteractedBlock the action to take with the interacted block
     */
    void setUseInteractedBlock(Result useInteractedBlock);

    /**
     * This controls the action to take with the item the player is holding.
     * This includes both blocks and items (such as flint and steel or
     * records). When this is set to default, it will be allowed if no action
     * is taken on the interacted block.
     *
     * @return the action to take with the item in hand
     */
    Result useItemInHand();

    /**
     * @param useItemInHand the action to take with the item in hand
     */
    void setUseItemInHand(Result useItemInHand);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

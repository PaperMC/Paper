package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class PlayerInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Action action;
    private final ItemStack item;
    private final Block blockClicked;
    private final BlockFace blockFace;
    private final EquipmentSlot hand;
    private final Vector clickedPosition;
    private Result useItemInHand;
    private Result useClickedBlock;

    @ApiStatus.Internal
    public PlayerInteractEvent(@NotNull final Player player, @NotNull final Action action, @Nullable final ItemStack item, @Nullable final Block clickedBlock, @NotNull final BlockFace clickedFace) {
        this(player, action, item, clickedBlock, clickedFace, EquipmentSlot.HAND);
    }

    @ApiStatus.Internal
    public PlayerInteractEvent(@NotNull final Player player, @NotNull final Action action, @Nullable final ItemStack item, @Nullable final Block clickedBlock, @NotNull final BlockFace clickedFace, @Nullable final EquipmentSlot hand) {
        this(player, action, item, clickedBlock, clickedFace, hand, null);
    }

    @ApiStatus.Internal
    public PlayerInteractEvent(@NotNull final Player player, @NotNull final Action action, @Nullable final ItemStack item, @Nullable final Block clickedBlock, @NotNull final BlockFace clickedFace, @Nullable final EquipmentSlot hand, @Nullable final Vector clickedPosition) {
        super(player);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.hand = hand;
        this.clickedPosition = clickedPosition;

        this.useItemInHand = Result.DEFAULT;
        this.useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
    }

    /**
     * Returns the action type
     *
     * @return Action returns the type of interaction
     */
    @NotNull
    public Action getAction() {
        return this.action;
    }

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
    @Deprecated(since = "1.14")
    @Override
    public boolean isCancelled() {
        return this.useInteractedBlock() == Result.DENY;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Canceling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.setUseInteractedBlock(cancel ? Result.DENY : this.useInteractedBlock() == Result.DENY ? Result.DEFAULT : this.useInteractedBlock());
        this.setUseItemInHand(cancel ? Result.DENY : this.useItemInHand() == Result.DENY ? Result.DEFAULT : this.useItemInHand());
    }

    /**
     * Returns the item in hand represented by this event
     *
     * @return ItemStack the item used
     */
    @Nullable
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Convenience method. Returns the material of the item represented by
     * this event
     *
     * @return Material the material of the item used
     */
    @NotNull
    public Material getMaterial() {
        if (!this.hasItem()) {
            return Material.AIR;
        }

        return this.item.getType();
    }

    /**
     * Check if this event involved a block
     *
     * @return boolean {@code true} if it did
     */
    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    /**
     * Check if this event involved an item
     *
     * @return boolean {@code true} if it did
     */
    public boolean hasItem() {
        return this.item != null;
    }

    /**
     * Convenience method to inform the user whether this was a block
     * placement event.
     *
     * @return boolean {@code true} if the item in hand was a block
     */
    public boolean isBlockInHand() {
        if (!this.hasItem()) {
            return false;
        }

        return this.item.getType().isBlock();
    }

    /**
     * Returns the clicked block
     *
     * @return Block returns the block clicked with this item.
     */
    @Nullable
    public Block getClickedBlock() {
        return this.blockClicked;
    }

    /**
     * Returns the face of the block that was clicked
     *
     * @return BlockFace returns the face of the block that was clicked
     */
    @NotNull
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    /**
     * The hand used to perform this interaction. May be {@code null} in the case of
     * {@link Action#PHYSICAL}.
     *
     * @return the hand used to interact. May be {@code null}.
     */
    @Nullable
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Gets the exact position on the block the player interacted with, this will
     * be {@code null} outside of {@link Action#RIGHT_CLICK_BLOCK}.
     * <p>
     * All vector components are between 0.0 and 1.0 inclusive.
     *
     * @deprecated misleading, use {@link #getInteractionPoint()}
     * @return the clicked position. May be {@code null}.
     */
    @Nullable
    @Deprecated
    public Vector getClickedPosition() {
        if (this.clickedPosition == null) {
            return null;
        }
        return this.clickedPosition.clone();
    }

    /**
     * The exact point at which the interaction occurred. May be {@code null}.
     *
     * @return the exact interaction point. May be {@code null}.
     */
    @Nullable
    public Location getInteractionPoint() {
        if (this.blockClicked == null || this.clickedPosition == null) {
            return null;
        }
        return this.blockClicked.getLocation().add(this.clickedPosition);
    }

    /**
     * This controls the action to take with the block (if any) that was
     * clicked on. This event gets processed for all blocks, but most don't
     * have a default action
     *
     * @return the action to take with the interacted block
     */
    @NotNull
    public Result useInteractedBlock() {
        return this.useClickedBlock;
    }

    /**
     * @param useInteractedBlock the action to take with the interacted block
     */
    public void setUseInteractedBlock(@NotNull Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    /**
     * This controls the action to take with the item the player is holding.
     * This includes both blocks and items (such as flint and steel or
     * records). When this is set to default, it will be allowed if no action
     * is taken on the interacted block.
     *
     * @return the action to take with the item in hand
     */
    @NotNull
    public Result useItemInHand() {
        return this.useItemInHand;
    }

    /**
     * @param useItemInHand the action to take with the item in hand
     */
    public void setUseItemInHand(@NotNull Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

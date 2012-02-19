package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;

/**
 * Called when a player interacts with an object or air.
 */
public class PlayerInteractEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected ItemStack item;
    protected Action action;
    protected Block blockClicked;
    protected BlockFace blockFace;
    private Result useClickedBlock;
    private Result useItemInHand;

    public PlayerInteractEvent(final Player who, final Action action, final ItemStack item, final Block clickedBlock, final BlockFace clickedFace) {
        super(who);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;

        useItemInHand = Result.DEFAULT;
        useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
    }

    /**
     * Returns the action type
     *
     * @return Action returns the type of interaction
     */
    public Action getAction() {
        return action;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you
     * want to prevent buckets from placing water and so forth
     *
     * @return boolean cancellation state
     */
    public boolean isCancelled() {
        return useInteractedBlock() == Result.DENY;
    }

    /**
     * Sets the cancellation state of this event. A canceled event will not
     * be executed in the server, but will still pass to other plugins
     * <p />
     * Canceling this event will prevent use of food (player won't lose the
     * food item), prevent bows/snowballs/eggs from firing, etc. (player won't
     * lose the ammo)
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        setUseInteractedBlock(cancel ? Result.DENY : useInteractedBlock() == Result.DENY ? Result.DEFAULT : useInteractedBlock());
        setUseItemInHand(cancel ? Result.DENY : useItemInHand() == Result.DENY ? Result.DEFAULT : useItemInHand());
    }

    /**
     * Returns the item in hand represented by this event
     *
     * @return ItemStack the item used
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Convenience method. Returns the material of the item represented by this
     * event
     *
     * @return Material the material of the item used
     */
    public Material getMaterial() {
        if (!hasItem()) {
            return Material.AIR;
        }

        return item.getType();
    }

    /**
     * Check if this event involved a block
     *
     * @return boolean true if it did
     */
    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    /**
     * Check if this event involved an item
     *
     * @return boolean true if it did
     */
    public boolean hasItem() {
        return this.item != null;
    }

    /**
     * Convenience method to inform the user whether this was a block placement
     * event.
     *
     * @return boolean true if the item in hand was a block
     */
    public boolean isBlockInHand() {
        if (!hasItem()) {
            return false;
        }

        return item.getType().isBlock();
    }

    /**
     * Returns the clicked block
     *
     * @return Block returns the block clicked with this item.
     */
    public Block getClickedBlock() {
        return blockClicked;
    }

    /**
     * Returns the face of the block that was clicked
     *
     * @return BlockFace returns the face of the block that was clicked
     */
    public BlockFace getBlockFace() {
        return blockFace;
    }

    /**
     * This controls the action to take with the block (if any) that was clicked on
     * This event gets processed for all blocks, but most don't have a default action
     *
     * @return the action to take with the interacted block
     */
    public Result useInteractedBlock() {
        return useClickedBlock;
    }

    /**
     * @param useInteractedBlock the action to take with the interacted block
     */
    public void setUseInteractedBlock(Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    /**
     * This controls the action to take with the item the player is holding
     * This includes both blocks and items (such as flint and steel or records)
     * When this is set to default, it will be allowed if no action is taken on the interacted block
     *
     * @return the action to take with the item in hand
     */
    public Result useItemInHand() {
        return useItemInHand;
    }

    /**
     * @param useItemInHand the action to take with the item in hand
     */
    public void setUseItemInHand(Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

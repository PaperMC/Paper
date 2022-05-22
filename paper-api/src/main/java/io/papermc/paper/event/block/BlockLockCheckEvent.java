package io.papermc.paper.event.block;

import com.google.common.base.Preconditions;
import io.papermc.paper.block.LockableTileState;
import java.util.Objects;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when the server tries to check the lock on a lockable block entity.
 * <br>
 * See {@link #setResult(Result)} to change behavior
 */
@NullMarked
public class BlockLockCheckEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private @Nullable Component lockedMessage;
    private @Nullable Sound lockedSound;
    private @Nullable ItemStack itemStack;
    private Result result = Result.DEFAULT;

    @ApiStatus.Internal
    public BlockLockCheckEvent(final Block block, final Player player, final Component lockedMessage, final Sound lockedSound) {
        super(block);
        this.player = player;
        this.lockedMessage = lockedMessage;
        this.lockedSound = lockedSound;
    }

    /**
     * Gets the snapshot {@link LockableTileState} of the block entity
     * whose lock is being checked.
     *
     * @return the snapshot block state.
     */
    public LockableTileState getBlockState() {
        final BlockState blockState = this.getBlock().getState();
        Preconditions.checkState(blockState instanceof LockableTileState, "Block state of lock-checked block is no longer a lockable tile state!");
        return (LockableTileState) blockState;
    }

    /**
     * Get the player involved this lock check.
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the itemstack that will be used as the key itemstack. Initially
     * this will be the item in the player's main hand but an override can be set
     * with {@link #setKeyItem(ItemStack)}. Use {@link #isUsingCustomKeyItemStack()}
     * to check if a custom key stack has been set.
     *
     * @return the item being used as the key item
     * @see #isUsingCustomKeyItemStack()
     */
    public ItemStack getKeyItem() {
        return Objects.requireNonNullElseGet(this.itemStack, this.player.getInventory()::getItemInMainHand);
    }

    /**
     * Sets the itemstack that will be used as the key item.
     *
     * @param stack the stack to use as a key
     * @see #resetKeyItem() to clear a custom key item
     */
    public void setKeyItem(final ItemStack stack) {
        Preconditions.checkArgument(stack != null, "stack cannot be null");
        this.itemStack = stack;
    }

    /**
     * Reset the key stack to the default (the player's main hand).
     */
    public void resetKeyItem() {
        this.itemStack = null;
    }

    /**
     * Checks if a custom key stack has been set.
     *
     * @return {@code true} if a custom key itemstack has been set
     */
    public boolean isUsingCustomKeyItemStack() {
        return this.itemStack != null;
    }

    /**
     * Gets the result of this event.
     *
     * @return the result
     * @see #setResult(Result)
     */
    public Result getResult() {
        return this.result;
    }

    /**
     * Gets the result of this event. {@link Result#DEFAULT} is the default
     * allowing the vanilla logic to check the lock of this block. Set to {@link Result#ALLOW}
     * or {@link Result#DENY} to override that behavior.
     * <p>
     * Setting this to {@link Result#ALLOW} bypasses the spectator check.
     *
     * @param result the result of this event
     */
    public void setResult(final Result result) {
        this.result = result;
    }

    /**
     * Shorthand method to set the {@link #getResult()} to {@link Result#DENY},
     * the locked message and locked sound.
     *
     * @param lockedMessage the message to show if locked (or {@code null} for none)
     * @param lockedSound   the sound to play if locked (or {@code null} for none)
     */
    public void denyWithMessageAndSound(final @Nullable Component lockedMessage, final @Nullable Sound lockedSound) {
        this.result = Result.DENY;
        this.lockedMessage = lockedMessage;
        this.lockedSound = lockedSound;
    }

    /**
     * Gets the locked message that will be sent if the
     * player cannot open the block.
     *
     * @return the locked message (or {@code null} if none)
     */
    public @Nullable Component getLockedMessage() {
        return this.lockedMessage;
    }

    /**
     * Sets the locked message that will be sent if the
     * player cannot open the block.
     *
     * @param lockedMessage the locked message (or {@code null} for none)
     */
    public void setLockedMessage(final @Nullable Component lockedMessage) {
        this.lockedMessage = lockedMessage;
    }

    /**
     * Gets the locked sound that will play if the
     * player cannot open the block.
     *
     * @return the locked sound (or {@code null} if none)
     */
    public @Nullable Sound getLockedSound() {
        return this.lockedSound;
    }

    /**
     * Sets the locked sound that will play if the
     * player cannot open the block.
     *
     * @param lockedSound the locked sound (or {@code null} for none)
     */
    public void setLockedSound(final @Nullable Sound lockedSound) {
        this.lockedSound = lockedSound;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

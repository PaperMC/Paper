package io.papermc.paper.event.block;

import io.papermc.paper.block.LockableTileState;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * Called when the server tries to check the lock on a lockable block entity.
 * <br>
 * See {@link #setResult(Result)} to change behavior
 */
public interface BlockLockCheckEvent extends BlockEventNew {

    /**
     * Gets the snapshot {@link LockableTileState} of the block entity
     * whose lock is being checked.
     *
     * @return the snapshot block state.
     */
    LockableTileState getBlockState();

    /**
     * Get the player involved this lock check.
     *
     * @return the player
     */
    Player getPlayer();

    /**
     * Gets the itemstack that will be used as the key itemstack. Initially
     * this will be the item in the player's main hand but an override can be set
     * with {@link #setKeyItem(ItemStack)}. Use {@link #isUsingCustomKeyItemStack()}
     * to check if a custom key stack has been set.
     *
     * @return the item being used as the key item
     * @see #isUsingCustomKeyItemStack()
     */
    ItemStack getKeyItem();

    /**
     * Sets the itemstack that will be used as the key item.
     *
     * @param stack the stack to use as a key
     * @see #resetKeyItem() to clear a custom key item
     */
    void setKeyItem(ItemStack stack);

    /**
     * Reset the key stack to the default (the player's main hand).
     */
    void resetKeyItem();

    /**
     * Checks if a custom key stack has been set.
     *
     * @return {@code true} if a custom key itemstack has been set
     */
    boolean isUsingCustomKeyItemStack();

    /**
     * Gets the result of this event.
     *
     * @return the result
     * @see #setResult(Result)
     */
    Result getResult();

    /**
     * Gets the result of this event. {@link Result#DEFAULT} is the default
     * allowing the vanilla logic to check the lock of this block. Set to {@link Result#ALLOW}
     * or {@link Result#DENY} to override that behavior.
     * <p>
     * Setting this to {@link Result#ALLOW} bypasses the spectator check.
     *
     * @param result the result of this event
     */
    void setResult(Result result);

    /**
     * Shorthand method to set the {@link #getResult()} to {@link Result#DENY},
     * the locked message and locked sound.
     *
     * @param lockedMessage the message to show if locked (or {@code null} for none)
     * @param lockedSound   the sound to play if locked (or {@code null} for none)
     */
    void denyWithMessageAndSound(@Nullable Component lockedMessage, @Nullable Sound lockedSound);

    /**
     * Gets the locked message that will be sent if the
     * player cannot open the block.
     *
     * @return the locked message (or {@code null} if none)
     */
    @Nullable Component getLockedMessage();

    /**
     * Sets the locked message that will be sent if the
     * player cannot open the block.
     *
     * @param lockedMessage the locked message (or {@code null} for none)
     */
    void setLockedMessage(@Nullable Component lockedMessage);

    /**
     * Gets the locked sound that will play if the
     * player cannot open the block.
     *
     * @return the locked sound (or {@code null} if none)
     */
    @Nullable Sound getLockedSound();

    /**
     * Sets the locked sound that will play if the
     * player cannot open the block.
     *
     * @param lockedSound the locked sound (or {@code null} for none)
     */
    void setLockedSound(@Nullable Sound lockedSound);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

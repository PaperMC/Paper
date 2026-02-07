package io.papermc.paper.event.block;

import com.google.common.base.Preconditions;
import io.papermc.paper.block.LockableTileState;
import java.util.Objects;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperBlockLockCheckEvent extends CraftBlockEvent implements BlockLockCheckEvent {

    private final Player player;
    private @Nullable Component lockedMessage;
    private @Nullable Sound lockedSound;
    private @Nullable ItemStack item;
    private Result result = Result.DEFAULT;
    private @Nullable LockableTileState lockableBlockEntity;

    public PaperBlockLockCheckEvent(final Block block, final Player player, final Component lockedMessage, final Sound lockedSound) {
        super(block);
        this.player = player;
        this.lockedMessage = lockedMessage;
        this.lockedSound = lockedSound;
    }

    @Override
    public LockableTileState getBlockState() {
        if (this.lockableBlockEntity == null) {
            if (this.getBlock().getState() instanceof final LockableTileState lockableBlockEntity) {
                this.lockableBlockEntity = lockableBlockEntity;
            } else {
                throw new IllegalArgumentException("Block state of lock-checked block is no longer a lockable tile state!");
            }
        }
        return this.lockableBlockEntity;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getKeyItem() {
        return Objects.requireNonNullElseGet(this.item, this.player.getInventory()::getItemInMainHand);
    }

    @Override
    public void setKeyItem(final ItemStack item) {
        Preconditions.checkArgument(item != null, "item cannot be null");
        this.item = item;
    }

    @Override
    public void resetKeyItem() {
        this.item = null;
    }

    @Override
    public boolean isUsingCustomKeyItemStack() {
        return this.item != null;
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(final Result result) {
        this.result = result;
    }

    @Override
    public void denyWithMessageAndSound(final @Nullable Component lockedMessage, final @Nullable Sound lockedSound) {
        this.result = Result.DENY;
        this.lockedMessage = lockedMessage;
        this.lockedSound = lockedSound;
    }

    @Override
    public @Nullable Component getLockedMessage() {
        return this.lockedMessage;
    }

    @Override
    public void setLockedMessage(final @Nullable Component lockedMessage) {
        this.lockedMessage = lockedMessage;
    }

    @Override
    public @Nullable Sound getLockedSound() {
        return this.lockedSound;
    }

    @Override
    public void setLockedSound(final @Nullable Sound lockedSound) {
        this.lockedSound = lockedSound;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockLockCheckEvent.getHandlerList();
    }
}

package io.papermc.paper.event.block;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.block.LockableTileState;
import io.papermc.paper.registry.keys.SoundEventKeys;
import java.util.Objects;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperBlockLockCheckEvent extends CraftBlockEvent implements BlockLockCheckEvent {

    @Deprecated(forRemoval = true)
    public static @Nullable BlockLockCheckEvent LAST_LOCKED_EVENT;

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

    public PaperBlockLockCheckEvent(final BlockEntity blockEntity, final ServerPlayer player, final net.minecraft.network.chat.Component displayName) {
        this(
            CraftBlock.at(blockEntity.getLevel(), blockEntity.getBlockPos()),
            player.getBukkitEntity(),
            Component.translatable("container.isLocked", PaperAdventure.asAdventure(displayName)),
            Sound.sound(SoundEventKeys.BLOCK_CHEST_LOCKED, Sound.Source.BLOCK, 1.0F, 1.0F)
        );
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

    public static boolean sendChestLockedNotifications(final Vec3 pos) {
        if (LAST_LOCKED_EVENT != null) {
            final BlockLockCheckEvent event = LAST_LOCKED_EVENT;
            LAST_LOCKED_EVENT = null;
            if (event.getLockedMessage() != null) {
                event.getPlayer().sendActionBar(event.getLockedMessage());
            }
            if (event.getLockedSound() != null) {
                event.getPlayer().getWorld().playSound(event.getLockedSound(), pos.x(), pos.y(), pos.z());
            }
            return true;
        }
        return false;
    }
}

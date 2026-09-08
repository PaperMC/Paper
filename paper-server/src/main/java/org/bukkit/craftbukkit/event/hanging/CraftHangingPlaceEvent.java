package org.bukkit.craftbukkit.event.hanging;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftHangingPlaceEvent extends CraftHangingEvent implements HangingPlaceEvent {

    private final @Nullable Player player;
    private final Block block;
    private final BlockFace blockFace;
    private final EquipmentSlot hand;
    private final @Nullable ItemStack itemStack;

    private boolean cancelled;

    public CraftHangingPlaceEvent(final Hanging hanging, final @Nullable Player player, final Block block, final BlockFace blockFace, final EquipmentSlot hand, final @Nullable ItemStack itemStack) {
        super(hanging);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
        this.hand = hand;
        this.itemStack = itemStack;
    }

    public CraftHangingPlaceEvent(
        final Entity hanging,
        final net.minecraft.world.entity.player.@Nullable Player player,
        final Level level,
        final BlockPos pos,
        final @Nullable Direction face,
        final InteractionHand hand,
        final net.minecraft.world.item.@Nullable ItemStack itemStack
    ) {
        this(
            (Hanging) hanging.getBukkitEntity(),
            (Player) Optionull.map(player, net.minecraft.world.entity.player.Player::getBukkitEntity),
            CraftBlock.at(level, pos),
            CraftBlock.notchToBlockFace(face),
            CraftEquipmentSlot.getHand(hand),
            Optionull.map(itemStack, CraftItemStack::asBukkitCopy)
        );
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public @Nullable ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HangingPlaceEvent.getHandlerList();
    }
}

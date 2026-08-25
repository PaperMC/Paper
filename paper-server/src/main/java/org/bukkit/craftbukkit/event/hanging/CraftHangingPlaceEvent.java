package org.bukkit.craftbukkit.event.hanging;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftHangingPlaceEvent extends CraftHangingEvent implements HangingPlaceEvent {

    private final Player player;
    private final Block block;
    private final BlockFace blockFace;
    private final EquipmentSlot hand;
    private final ItemStack itemStack;

    private boolean cancelled;

    public CraftHangingPlaceEvent(final Hanging hanging, final @Nullable Player player, final Block block, final BlockFace blockFace, final @Nullable EquipmentSlot hand, final @Nullable ItemStack itemStack) {
        super(hanging);
        this.player = player;
        this.block = block;
        this.blockFace = blockFace;
        this.hand = hand;
        this.itemStack = itemStack;
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
    public @Nullable EquipmentSlot getHand() {
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

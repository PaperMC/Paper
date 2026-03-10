package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public class PaperPlayerSwapWithEquipmentSlotEvent extends CraftPlayerEvent implements PlayerSwapWithEquipmentSlotEvent {

    private final ItemStack itemInHand;
    private final EquipmentSlot slot;
    private final ItemStack itemToSwap;

    private boolean cancelled;

    @ApiStatus.Internal
    public PaperPlayerSwapWithEquipmentSlotEvent(
        final Player player,
        final ItemStack itemInHand,
        final EquipmentSlot slot,
        final ItemStack itemToSwap
    ) {
        super(player);
        this.itemInHand = itemInHand;
        this.slot = slot;
        this.itemToSwap = itemToSwap;
    }

    @Override
    public ItemStack getItemInHand() {
        return this.itemInHand.clone();
    }

    @Override
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    @Override
    public ItemStack getItemToSwap() {
        return this.itemToSwap.clone();
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
        return PlayerSwapWithEquipmentSlotEvent.getHandlerList();
    }
}

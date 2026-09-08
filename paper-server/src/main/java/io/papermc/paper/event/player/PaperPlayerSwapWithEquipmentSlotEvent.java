package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerSwapWithEquipmentSlotEvent extends CraftPlayerEvent implements PlayerSwapWithEquipmentSlotEvent {

    private final ItemStack itemInHand;
    private final EquipmentSlot slot;
    private final ItemStack itemToSwap;

    private boolean cancelled;

    public PaperPlayerSwapWithEquipmentSlotEvent(final Player player, final ItemStack itemInHand, final EquipmentSlot slot, final ItemStack itemToSwap) {
        super(player);
        this.itemInHand = itemInHand;
        this.slot = slot;
        this.itemToSwap = itemToSwap;
    }

    public PaperPlayerSwapWithEquipmentSlotEvent(
        final net.minecraft.world.entity.player.Player player,
        final net.minecraft.world.item.ItemStack itemInHand,
        final net.minecraft.world.entity.EquipmentSlot slot,
        final net.minecraft.world.item.ItemStack itemToSwap
    ) {
        this((Player) player.getBukkitEntity(),
            CraftItemStack.asCraftMirror(itemInHand),
            CraftEquipmentSlot.getSlot(slot),
            CraftItemStack.asCraftMirror(itemToSwap)
        );
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

package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperPlayerInventorySlotChangeEvent extends CraftPlayerEvent implements PlayerInventorySlotChangeEvent {

    private final int rawSlot;
    private final InventoryView view;
    private final ItemStack oldItemStack;
    private final ItemStack newItemStack;

    private @Nullable Integer slot;
    private boolean triggerAdvancements = true;

    public PaperPlayerInventorySlotChangeEvent(final Player player, final int rawSlot, final ItemStack oldItemStack, final ItemStack newItemStack) {
        super(player);
        this.rawSlot = rawSlot;
        this.view = player.getOpenInventory();
        this.oldItemStack = oldItemStack;
        this.newItemStack = newItemStack;
    }

    @Override
    public int getRawSlot() {
        return this.rawSlot;
    }

    @Override
    public int getSlot() {
        if (this.slot == null) {
            this.slot = this.view.convertSlot(this.rawSlot);
        }
        return this.slot;
    }

    @Override
    public ItemStack getOldItemStack() {
        return this.oldItemStack;
    }

    @Override
    public ItemStack getNewItemStack() {
        return this.newItemStack;
    }

    @Override
    public boolean shouldTriggerAdvancements() {
        return this.triggerAdvancements;
    }

    @Override
    public void setShouldTriggerAdvancements(final boolean triggerAdvancements) {
        this.triggerAdvancements = triggerAdvancements;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerInventorySlotChangeEvent.getHandlerList();
    }
}

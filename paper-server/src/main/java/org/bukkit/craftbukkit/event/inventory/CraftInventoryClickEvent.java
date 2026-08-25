package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftInventoryClickEvent extends CraftInventoryInteractEvent implements InventoryClickEvent {

    private final ClickType click;
    private final InventoryAction action;
    private final InventoryType.SlotType slotType;
    private final int whichSlot;
    private final int rawSlot;
    private ItemStack current = null;
    private int hotbarKey = -1;

    public CraftInventoryClickEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action) {
        super(view);
        this.slotType = type;
        this.rawSlot = slot;
        this.whichSlot = view.convertSlot(slot);
        this.click = click;
        this.action = action;
    }

    public CraftInventoryClickEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action, final int key) {
        this(view, type, slot, click, action);
        this.hotbarKey = key;
    }

    @Override
    public InventoryType.SlotType getSlotType() {
        return this.slotType;
    }

    @Override
    public @Nullable ItemStack getCurrentItem() {
        if (this.slotType == InventoryType.SlotType.OUTSIDE) {
            return this.current;
        }
        return this.getView().getItem(this.rawSlot);
    }

    @Override
    public boolean isRightClick() {
        return this.click.isRightClick();
    }

    @Override
    public boolean isLeftClick() {
        return this.click.isLeftClick();
    }

    @Override
    public boolean isShiftClick() {
        return this.click.isShiftClick();
    }

    @Override
    public void setCurrentItem(final @Nullable ItemStack stack) {
        if (this.slotType == InventoryType.SlotType.OUTSIDE) {
            this.current = stack;
        } else {
            this.getView().setItem(this.rawSlot, stack);
        }
    }

    @Override
    public @Nullable Inventory getClickedInventory() {
        return this.getView().getInventory(this.rawSlot);
    }

    @Override
    public int getSlot() {
        return this.whichSlot;
    }

    @Override
    public int getRawSlot() {
        return this.rawSlot;
    }

    @Override
    public int getHotbarButton() {
        return this.hotbarKey;
    }

    @Override
    public InventoryAction getAction() {
        return this.action;
    }

    @Override
    public ClickType getClick() {
        return this.click;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryClickEvent.getHandlerList();
    }
}

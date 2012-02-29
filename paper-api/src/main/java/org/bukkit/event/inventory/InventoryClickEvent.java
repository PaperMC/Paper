package org.bukkit.event.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEvent extends InventoryEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private SlotType slot_type;
    private boolean rightClick, shiftClick;
    private Result result;
    private int whichSlot;
    private int rawSlot;
    private ItemStack current = null;

    public InventoryClickEvent(InventoryView what, SlotType type, int slot, boolean right, boolean shift) {
        super(what);
        this.slot_type = type;
        this.rightClick = right;
        this.shiftClick = shift;
        this.result = Result.DEFAULT;
        this.rawSlot = slot;
        this.whichSlot = what.convertSlot(slot);
    }

    /**
     * Get the type of slot that was clicked.
     * @return The slot type.
     */
    public SlotType getSlotType() {
        return slot_type;
    }

    /**
     * Get the current item on the cursor.
     * @return The cursor item
     */
    public ItemStack getCursor() {
        return getView().getCursor();
    }

    /**
     * Get the current item in the clicked slot.
     * @return The slot item.
     */
    public ItemStack getCurrentItem() {
        if(slot_type == SlotType.OUTSIDE) return current;
        return getView().getItem(rawSlot);
    }

    /**
     * @return True if the click is a right-click.
     */
    public boolean isRightClick() {
        return rightClick;
    }

    /**
     * @return True if the click is a left-click.
     */
    public boolean isLeftClick() {
        return !rightClick;
    }

    /**
     * Shift can be combined with right-click or left-click as a modifier.
     * @return True if the click is a shift-click.
     */
    public boolean isShiftClick() {
        return shiftClick;
    }

    public void setResult(Result newResult) {
        result = newResult;
    }

    public Result getResult() {
        return result;
    }

    /**
     * Get the player who performed the click.
     * @return The clicking player.
     */
    public HumanEntity getWhoClicked() {
        return getView().getPlayer();
    }

    /**
     * Set the item on the cursor.
     * @param what The new cursor item.
     */
    public void setCursor(ItemStack what) {
        getView().setCursor(what);
    }

    /**
     * Set the current item in the slot.
     * @param what The new slot item.
     */
    public void setCurrentItem(ItemStack what) {
        if(slot_type == SlotType.OUTSIDE) current = what;
        else getView().setItem(rawSlot, what);
    }

    public boolean isCancelled() {
        return result == Result.DENY;
    }

    public void setCancelled(boolean toCancel) {
        result = toCancel ? Result.DENY : Result.ALLOW;
    }

    /**
     * The slot number that was clicked, ready for passing to {@link Inventory#getItem(int)}. Note
     * that there may be two slots with the same slot number, since a view links two different inventories.
     * @return The slot number.
     */
    public int getSlot() {
        return whichSlot;
    }

    /**
     * The raw slot number, which is unique for the view.
     * @return The slot number.
     */
    public int getRawSlot() {
        return rawSlot;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jspecify.annotations.Nullable;

/**
 * This event is called when a player clicks in an inventory.
 * <p>
 * In case of a drag action within an inventory, InventoryClickEvent is never called.
 * Instead, {@link InventoryDragEvent} is called at the end of the drag.
 * <p>
 * Because InventoryClickEvent occurs within a modification of the Inventory,
 * not all Inventory related methods are safe to use.
 * <p>
 * Methods that change the view a player is looking at should never be invoked
 * by an EventHandler for InventoryClickEvent using the HumanEntity or
 * InventoryView associated with this event.
 * Examples of these include:
 * <ul>
 * <li>{@link HumanEntity#closeInventory()}
 * <li>{@link HumanEntity#openInventory(Inventory)}
 * <li>{@link InventoryView#close()}
 * </ul>
 * To invoke one of these methods, schedule a task using
 * {@link BukkitScheduler#runTask(Plugin, Runnable)}, which will run the task
 * on the next tick. Also be aware that this is not an exhaustive list, and
 * other methods could potentially create issues as well.
 * <p>
 * Assuming the EntityHuman associated with this event is an instance of a
 * Player, manipulating the MaxStackSize or contents of an Inventory will
 * require an Invocation of {@link Player#updateInventory()}.
 * <p>
 * Modifications to slots that are modified by the results of this
 * InventoryClickEvent can be overwritten. To change these slots, this event
 * should be cancelled and all desired changes to the inventory applied.
 * Alternatively, scheduling a task using {@link BukkitScheduler#runTask(
 * Plugin, Runnable)}, which would execute the task on the next tick, would
 * work as well.
 */
public interface InventoryClickEvent extends InventoryInteractEvent {

    /**
     * Gets the type of slot that was clicked.
     *
     * @return the slot type
     */
    SlotType getSlotType();

    /**
     * Gets the current ItemStack on the cursor.
     *
     * @return the cursor ItemStack
     */
    default ItemStack getCursor() {
        return this.getView().getCursor();
    }

    /**
     * Sets the item on the cursor.
     *
     * @param stack the new cursor item
     * @deprecated This changes the ItemStack in their hand before any
     *     calculations are applied to the Inventory, which has a tendency to
     *     create inconsistencies between the Player and the server, and to
     *     make unexpected changes in the behavior of the clicked Inventory.
     */
    @Deprecated(since = "1.5.2")
    default void setCursor(final @Nullable ItemStack stack) {
        this.getView().setCursor(stack);
    }

    /**
     * Gets the ItemStack currently in the clicked slot.
     *
     * @return the item in the clicked slot
     */
    @Nullable ItemStack getCurrentItem();

    /**
     * Gets whether the ClickType for this event represents a right
     * click.
     *
     * @return {@code true} if the ClickType uses the right mouse button.
     * @see ClickType#isRightClick()
     */
    boolean isRightClick();

    /**
     * Gets whether the ClickType for this event represents a left
     * click.
     *
     * @return {@code true} if the ClickType uses the left mouse button.
     * @see ClickType#isLeftClick()
     */
    boolean isLeftClick();

    /**
     * Gets whether the ClickType for this event indicates that the key was
     * pressed down when the click was made.
     *
     * @return {@code true} if the ClickType uses Shift or Ctrl.
     * @see ClickType#isShiftClick()
     */
    boolean isShiftClick();

    /**
     * Sets the ItemStack currently in the clicked slot.
     *
     * @param stack the item to be placed in the current slot
     */
    void setCurrentItem(@Nullable ItemStack stack);

    /**
     * Gets the inventory corresponding to the clicked slot.
     *
     * @return inventory, or {@code null} if clicked outside
     * @see InventoryView#getInventory(int)
     */
    @Nullable Inventory getClickedInventory();

    /**
     * The slot number that was clicked, ready for passing to
     * {@link Inventory#getItem(int)}. Note that there may be two slots with
     * the same slot number, since a view links two different inventories.
     *
     * @return the slot number
     */
    int getSlot();

    /**
     * The raw slot number clicked, ready for passing to {@link InventoryView
     * #getItem(int)} This slot number is unique for the view.
     *
     * @return the slot number
     */
    int getRawSlot();

    /**
     * If the ClickType is NUMBER_KEY, this method will return the index of
     * the pressed key (0-8) and -1 if player swapped with off-hand (or the action is not NUMBER_KEY).
     *
     * @return the number on the key minus 1 (range 0-8);
     * or -1 if ClickType is NUMBER_KEY and player did an off-hand swap. Is also -1 if ClickType is not NUMBER_KEY
     */
    int getHotbarButton();

    /**
     * Gets the InventoryAction that triggered this event.
     * <p>
     * This action cannot be changed, and represents what the normal outcome
     * of the event will be. To change the behavior of this
     * InventoryClickEvent, changes must be manually applied.
     *
     * @return the InventoryAction that triggered this event.
     */
    InventoryAction getAction();

    /**
     * Gets the ClickType for this event.
     * <p>
     * This is insulated against changes to the inventory by other plugins.
     *
     * @return the type of inventory click
     */
    ClickType getClick();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

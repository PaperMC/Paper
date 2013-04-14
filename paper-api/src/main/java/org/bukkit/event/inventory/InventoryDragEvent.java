package org.bukkit.event.inventory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.ImmutableSet;

/**
 * This event is called when the player drags an item in their cursor across
 * the inventory. The ItemStack is distributed across the slots the
 * HumanEntity dragged over. The method of distribution is described by the 
 * DragType returned by {@link #getType()}.
 * <p>
 * Canceling this event will result in none of the changes described in
 * {@link #getNewItems()} being applied to the Inventory.
 * <p>
 * Because InventoryDragEvent occurs within a modification of the Inventory,
 * not all Inventory related methods are safe to use.
 * <p>
 * The following should never be invoked by an EventHandler for
 * InventoryDragEvent using the HumanEntity or InventoryView associated with
 * this event.
 * <ul>
 * <li>{@link HumanEntity#closeInventory()}
 * <li>{@link HumanEntity#openInventory(Inventory)}
 * <li>{@link HumanEntity#openWorkbench(Location, boolean)}
 * <li>{@link HumanEntity#openEnchanting(Location, boolean)}
 * <li>{@link InventoryView#close()}
 * </ul>
 * To invoke one of these methods, schedule a task using 
 * {@link BukkitScheduler#runTask(Plugin, Runnable)}, which will run the task
 * on the next tick.  Also be aware that this is not an exhaustive list, and
 * other methods could potentially create issues as well.
 * <p>
 * Assuming the EntityHuman associated with this event is an instance of a
 * Player, manipulating the MaxStackSize or contents of an Inventory will
 * require an Invocation of {@link Player#updateInventory()}.
 * <p>
 * Any modifications to slots that are modified by the results of this
 * InventoryDragEvent will be overwritten. To change these slots, this event
 * should be cancelled and the changes applied. Alternatively, scheduling a
 * task using {@link BukkitScheduler#runTask(Plugin, Runnable)}, which would
 * execute the task on the next tick, would work as well.
 */
public class InventoryDragEvent extends InventoryInteractEvent {
    private static final HandlerList handlers = new HandlerList();
    private final DragType type;
    private final Map<Integer, ItemStack> addedItems;
    private final Set<Integer> containerSlots;
    private final ItemStack oldCursor;
    private ItemStack newCursor;

    public InventoryDragEvent(InventoryView what, ItemStack newCursor, ItemStack oldCursor, boolean right, Map<Integer, ItemStack> slots) {
        super(what);

        Validate.notNull(oldCursor);
        Validate.notNull(slots);

        type = right ? DragType.SINGLE : DragType.EVEN;
        this.newCursor = newCursor;
        this.oldCursor = oldCursor;
        this.addedItems = slots;
        ImmutableSet.Builder<Integer> b = ImmutableSet.builder();
        for (Integer slot : slots.keySet()) {
            b.add(what.convertSlot(slot));
        }
        this.containerSlots = b.build();
    }

    /**
     * Gets all items to be added to the inventory in this drag.
     *
     * @return map from raw slot id to new ItemStack
     */
    public Map<Integer, ItemStack> getNewItems() {
        return Collections.unmodifiableMap(addedItems);
    }

    /**
     * Gets the raw slot ids to be changed in this drag.
     *
     * @return list of raw slot ids, suitable for getView().getItem(int)
     */
    public Set<Integer> getRawSlots() {
        return addedItems.keySet();
    }

    /**
     * Gets the slots to be changed in this drag.
     *
     * @return list of converted slot ids, suitable for {@link
     *     org.bukkit.inventory.Inventory#getItem(int)}.
     */
    public Set<Integer> getInventorySlots() {
        return containerSlots;
    }

    /**
     * Gets the result cursor after the drag is done. The returned value is
     * mutable.
     *
     * @return the result cursor
     */
    public ItemStack getCursor() {
        return newCursor;
    }

    /**
     * Sets the result cursor after the drag is done.
     * <p>
     * Changing this item stack changes the cursor item. Note that changing
     * the affected "dragged" slots does not change this ItemStack, nor does
     * changing this ItemStack affect the "dragged" slots.
     *
     * @param newCursor the new cursor ItemStack
     */
    public void setCursor(ItemStack newCursor) {
        this.newCursor = newCursor;
    }

    /**
     * Gets an ItemStack representing the cursor prior to any modifications
     * as a result of this drag.
     *
     * @return the original cursor
     */
    public ItemStack getOldCursor() {
        return oldCursor.clone();
    }

    /**
     * Gets the DragType that describes the behavior of ItemStacks placed
     * after this InventoryDragEvent.
     * <p>
     * The ItemStacks and the raw slots that they're being applied to can be
     * found using {@link #getNewItems()}.
     *
     * @return the DragType of this InventoryDragEvent
     */
    public DragType getType() {
        return type;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

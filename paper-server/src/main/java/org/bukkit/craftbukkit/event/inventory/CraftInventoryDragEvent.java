package org.bukkit.craftbukkit.event.inventory;

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftInventoryDragEvent extends CraftInventoryInteractEvent implements InventoryDragEvent {

    private final DragType type;
    private final Map<Integer, ItemStack> addedItems;
    private final Set<Integer> containerSlots;
    private final ItemStack oldCursor;
    private ItemStack newCursor;

    public CraftInventoryDragEvent(final InventoryView view, final @Nullable ItemStack newCursor, final ItemStack oldCursor, final boolean right, final Map<Integer, ItemStack> slots) {
        super(view);

        this.type = right ? DragType.SINGLE : DragType.EVEN;
        this.newCursor = newCursor;
        this.oldCursor = oldCursor;
        this.addedItems = slots;
        final ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
        for (final Integer slot : slots.keySet()) {
            builder.add(view.convertSlot(slot));
        }
        this.containerSlots = builder.build();
    }

    @Override
    public DragType getType() {
        return this.type;
    }

    @Override
    public @Nullable ItemStack getCursor() {
        return this.newCursor;
    }

    @Override
    public void setCursor(final @Nullable ItemStack newCursor) {
        this.newCursor = newCursor;
    }

    @Override
    public ItemStack getOldCursor() {
        return this.oldCursor.clone();
    }

    @Override
    public Map<Integer, ItemStack> getNewItems() {
        return Collections.unmodifiableMap(this.addedItems);
    }

    @Override
    public Set<Integer> getRawSlots() {
        return Collections.unmodifiableSet(this.addedItems.keySet());
    }

    @Override
    public Set<Integer> getInventorySlots() {
        return this.containerSlots;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryDragEvent.getHandlerList();
    }
}

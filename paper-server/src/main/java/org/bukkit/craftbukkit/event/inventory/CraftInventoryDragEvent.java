package org.bukkit.craftbukkit.event.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public class CraftInventoryDragEvent extends CraftInventoryInteractEvent implements InventoryDragEvent {

    private final DragType type;
    private final ItemStack oldCursor;
    private final Map<Integer, ItemStack> draggedItems;
    private final Set<Integer> containerSlots;
    private @Nullable ItemStack newCursor;

    public CraftInventoryDragEvent(
        final InventoryView view,
        final DragType type,
        final ItemStack oldCursor,
        final ItemStack newCursor,
        final Map<Integer, ItemStack> draggedItems,
        final Set<Integer> containerSlots
    ) {
        super(view);
        this.type = type;
        this.oldCursor = oldCursor;
        this.newCursor = newCursor;
        this.draggedItems = Collections.unmodifiableMap(draggedItems);
        this.containerSlots = Collections.unmodifiableSet(containerSlots);
    }

    public CraftInventoryDragEvent(
        final AbstractContainerMenu menu,
        final boolean single,
        final net.minecraft.world.item.ItemStack oldCursor,
        final net.minecraft.world.item.ItemStack newCursor,
        final Int2ObjectMap<net.minecraft.world.item.ItemStack> slots
    ) {
        final InventoryView view = menu.getBukkitView();
        final Map<Integer, ItemStack> draggedItems = new HashMap<>(slots.size());
        final IntSet containerSlots = new IntArraySet(slots.size());
        slots.forEach((slot, item) -> {
            draggedItems.put(slot, CraftItemStack.asBukkitCopy(item));
            containerSlots.add(view.convertSlot(slot));
        });
        this(
            menu.getBukkitView(),
            single ? DragType.SINGLE : DragType.EVEN,
            CraftItemStack.asBukkitCopy(oldCursor),
            CraftItemStack.asCraftMirror(newCursor),
            draggedItems,
            containerSlots
        );
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
    public @Unmodifiable Map<Integer, ItemStack> getNewItems() {
        return this.draggedItems;
    }

    @Override
    public @Unmodifiable Set<Integer> getRawSlots() {
        return this.draggedItems.keySet();
    }

    @Override
    public @Unmodifiable Set<Integer> getInventorySlots() {
        return this.containerSlots;
    }

    @Override
    public HandlerList getHandlers() {
        return InventoryDragEvent.getHandlerList();
    }
}

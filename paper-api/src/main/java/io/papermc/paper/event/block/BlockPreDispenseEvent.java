package io.papermc.paper.event.block;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface BlockPreDispenseEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the {@link ItemStack} to be dispensed.
     *
     * @return The item to be dispensed
     */
    ItemStack getItemStack();

    /**
     * Gets the inventory slot of the dispenser to dispense from.
     *
     * @return The inventory slot
     */
    int getSlot();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

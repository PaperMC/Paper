package io.papermc.paper.event.inventory;

import com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

@Deprecated(since = "1.16.1")
public class PaperPrepareGrindstoneEvent extends PaperPrepareResultEvent implements PrepareGrindstoneEvent {

    public PaperPrepareGrindstoneEvent(final InventoryView inventory, final @Nullable ItemStack result) {
        super(inventory, result);
    }

    @Override
    public GrindstoneInventory getInventory() {
        return (GrindstoneInventory) super.getInventory();
    }
}

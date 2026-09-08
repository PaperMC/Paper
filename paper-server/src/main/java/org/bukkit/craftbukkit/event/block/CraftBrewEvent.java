package org.bukkit.craftbukkit.event.block;

import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftBrewEvent extends CraftBlockEvent implements BrewEvent {

    private final BrewerInventory contents;
    private final List<ItemStack> results;
    private final int fuelLevel;

    private boolean cancelled;

    public CraftBrewEvent(final Block brewer, final BrewerInventory contents, final List<ItemStack> results, final int fuelLevel) {
        super(brewer);
        this.contents = contents;
        this.results = results;
        this.fuelLevel = fuelLevel;
    }

    public CraftBrewEvent(final Level level, final BlockPos pos, final InventoryHolder owner, final List<net.minecraft.world.item.ItemStack> results, final int fuelLevel) {
        this(
            CraftBlock.at(level, pos),
            (BrewerInventory) owner.getInventory(),
            MCUtil.mutableTransform(results, CraftItemStack::asCraftMirror, CraftItemStack::asNMSCopy),
            fuelLevel
        );
    }

    @Override
    public BrewerInventory getContents() {
        return this.contents;
    }

    @Override
    public List<ItemStack> getResults() {
        return this.results;
    }

    @Override
    public int getFuelLevel() {
        return this.fuelLevel;
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
        return BrewEvent.getHandlerList();
    }
}

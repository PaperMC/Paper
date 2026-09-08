package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.player.Inventory;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

import static io.papermc.paper.util.BoundChecker.requireRange;

public abstract class PaperPlayerPickItemEvent extends CraftPlayerEvent implements PlayerPickItemEvent {

    private final ItemStack item;
    private final boolean includeData;

    private int targetSlot;
    private int sourceSlot;

    private boolean cancelled;

    protected PaperPlayerPickItemEvent(final Player player, final ItemStack item, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player);
        this.item = item;
        this.includeData = includeData;
        this.targetSlot = targetSlot;
        this.sourceSlot = sourceSlot;
    }

    @Override
    public ItemStack getItem() {
        return this.item.clone();
    }

    @Override
    public boolean isIncludeData() {
        return this.includeData;
    }

    @Override
    public @IntRange(from = 0, to = Inventory.SELECTION_SIZE - 1) int getTargetSlot() {
        return this.targetSlot;
    }

    @Override
    public void setTargetSlot(final @IntRange(from = 0, to = Inventory.SELECTION_SIZE - 1) int targetSlot) {
        this.targetSlot = requireRange(targetSlot, "targetSlot", 0, Inventory.SELECTION_SIZE - 1);
    }

    @Override
    public @IntRange(from = Inventory.NOT_FOUND_INDEX, to = Inventory.INVENTORY_SIZE - 1) int getSourceSlot() {
        return this.sourceSlot;
    }

    @Override
    public void setSourceSlot(final @IntRange(from = Inventory.NOT_FOUND_INDEX, to = Inventory.INVENTORY_SIZE - 1) int sourceSlot) {
        Preconditions.checkArgument(
            sourceSlot == Inventory.NOT_FOUND_INDEX || (sourceSlot >= 0 && sourceSlot < Inventory.INVENTORY_SIZE),
            "Source slot must be in range of the player's inventory slot: 0 - %s (inclusive), or %s", Inventory.INVENTORY_SIZE - 1, Inventory.NOT_FOUND_INDEX
        );
        this.sourceSlot = sourceSlot;
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
        return PlayerPickItemEvent.getHandlerList();
    }
}

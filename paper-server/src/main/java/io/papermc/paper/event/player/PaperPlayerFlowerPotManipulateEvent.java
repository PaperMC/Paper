package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperPlayerFlowerPotManipulateEvent extends CraftPlayerEvent implements PlayerFlowerPotManipulateEvent {

    private final Block flowerpot;
    private final ItemStack item;
    private final boolean placing;

    private boolean cancelled;

    @ApiStatus.Internal
    public PaperPlayerFlowerPotManipulateEvent(final Player player, final Block flowerpot, final ItemStack item, final boolean placing) {
        super(player);
        this.flowerpot = flowerpot;
        this.item = item;
        this.placing = placing;
    }

    @Override
    public Block getFlowerpot() {
        return this.flowerpot;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public boolean isPlacing() {
        return this.placing;
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
        return PlayerFlowerPotManipulateEvent.getHandlerList();
    }
}

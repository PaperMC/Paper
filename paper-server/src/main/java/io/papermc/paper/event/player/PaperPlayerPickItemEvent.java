package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public abstract class PaperPlayerPickItemEvent extends CraftPlayerEvent implements PlayerPickItemEvent {

    private final boolean includeData;

    private int targetSlot;
    private int sourceSlot;

    private boolean cancelled;

    protected PaperPlayerPickItemEvent(final Player player, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player);
        this.includeData = includeData;
        this.targetSlot = targetSlot;
        this.sourceSlot = sourceSlot;
    }

    @Override
    public boolean isIncludeData() {
        return includeData;
    }

    @Override
    public int getTargetSlot() {
        return this.targetSlot;
    }

    @Override
    public void setTargetSlot(final int targetSlot) {
        Preconditions.checkArgument(targetSlot >= 0 && targetSlot <= 8, "Target slot must be in range 0 - 8 (inclusive)");
        this.targetSlot = targetSlot;
    }

    @Override
    public int getSourceSlot() {
        return this.sourceSlot;
    }

    @Override
    public void setSourceSlot(final int sourceSlot) {
        Preconditions.checkArgument(sourceSlot >= -1 && sourceSlot <= 35, "Source slot must be in range of the player's inventory slot, or -1");
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

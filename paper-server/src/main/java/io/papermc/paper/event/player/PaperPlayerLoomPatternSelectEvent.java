package io.papermc.paper.event.player;

import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.LoomInventory;

public class PaperPlayerLoomPatternSelectEvent extends CraftPlayerEvent implements PlayerLoomPatternSelectEvent {

    private final LoomInventory loomInventory;
    private PatternType patternType;

    private boolean cancelled;

    public PaperPlayerLoomPatternSelectEvent(final Player player, final LoomInventory loomInventory, final PatternType patternType) {
        super(player);
        this.loomInventory = loomInventory;
        this.patternType = patternType;
    }

    @Override
    public LoomInventory getLoomInventory() {
        return this.loomInventory;
    }

    @Override
    public PatternType getPatternType() {
        return this.patternType;
    }

    @Override
    public void setPatternType(final PatternType patternType) {
        this.patternType = patternType;
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
        return PlayerLoomPatternSelectEvent.getHandlerList();
    }
}

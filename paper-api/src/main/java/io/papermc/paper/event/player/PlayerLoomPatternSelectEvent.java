package io.papermc.paper.event.player;

import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.LoomInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player selects a banner pattern in a loom inventory.
 */
@NullMarked
public class PlayerLoomPatternSelectEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LoomInventory loomInventory;
    private PatternType patternType;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerLoomPatternSelectEvent(final Player player, final LoomInventory loomInventory, final PatternType patternType) {
        super(player);
        this.loomInventory = loomInventory;
        this.patternType = patternType;
    }

    /**
     * Gets the loom inventory involved.
     *
     * @return the loom inventory
     */
    public LoomInventory getLoomInventory() {
        return this.loomInventory;
    }

    /**
     * Gets the pattern type selected.
     *
     * @return the pattern type
     */
    public PatternType getPatternType() {
        return this.patternType;
    }

    /**
     * Sets the pattern type selected.
     *
     * @param patternType the pattern type
     */
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

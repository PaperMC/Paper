package io.papermc.paper.event.player;

import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.LoomInventory;

/**
 * Called when a player selects a banner pattern in a loom inventory.
 */
public interface PlayerLoomPatternSelectEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the loom inventory involved.
     *
     * @return the loom inventory
     */
    LoomInventory getLoomInventory();

    /**
     * Gets the pattern type selected.
     *
     * @return the pattern type
     */
    PatternType getPatternType();

    /**
     * Sets the pattern type selected.
     *
     * @param patternType the pattern type
     */
    void setPatternType(PatternType patternType);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

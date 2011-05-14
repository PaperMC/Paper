package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This event is fired when the player is almost about to enter the bed.
 * It can be cancelled.
 *
 * @author sk89q
 */
public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    private boolean cancel = false;
    private Block bed;

    public PlayerBedEnterEvent(Player who, Block bed) {
        super(Type.PLAYER_BED_ENTER, who);
        this.bed = bed;
    }

    /**
     * Gets the cancellation state of this event.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Prevents the player from entering the bed.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the bed block.
     *
     * @return
     */
    public Block getBed() {
        return bed;
    }
}

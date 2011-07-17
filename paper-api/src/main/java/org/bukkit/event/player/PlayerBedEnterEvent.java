package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This event is fired when the player is almost about to enter the bed.
 */
public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    private boolean cancel = false;
    private Block bed;

    public PlayerBedEnterEvent(Player who, Block bed) {
        super(Type.PLAYER_BED_ENTER, who);
        this.bed = bed;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns the bed block involved in this event.
     *
     * @return the bed block involved in this event
     */
    public Block getBed() {
        return bed;
    }
}

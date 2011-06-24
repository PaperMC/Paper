package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * This event is fired when the player is leaving a bed.
 */
public class PlayerBedLeaveEvent extends PlayerEvent {

    private Block bed;

    public PlayerBedLeaveEvent(Player who, Block bed) {
        super(Type.PLAYER_BED_LEAVE, who);
        this.bed = bed;
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

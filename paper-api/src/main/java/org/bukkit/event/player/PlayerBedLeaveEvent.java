package org.bukkit.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * This event is fired when the player is leaving a bed.
 * 
 * @author sk89q
 */
public class PlayerBedLeaveEvent extends PlayerEvent {
    
    private Block bed;

    public PlayerBedLeaveEvent(Player who, Block bed) {
        super(Type.PLAYER_BED_LEAVE, who);
        this.bed = bed;
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

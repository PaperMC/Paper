package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.Player;

/**
 * Not implemented yet
 */
public class BlockBrokenEvent extends BlockEvent {
    private Player player;

    public BlockBrokenEvent(Type type, Block block ) {
        super(type, block);
    }
    
    public Player getPlayer() {
        return player;
    }
}

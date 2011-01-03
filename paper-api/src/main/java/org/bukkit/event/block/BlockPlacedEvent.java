package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.Player;
import org.bukkit.event.Cancellable;

/**
 * Not implemented yet
 */
public class BlockPlacedEvent extends BlockEvent implements Cancellable {
    private boolean cancel;
    private Player player;

    /**
     * @param type
     * @param theBlock
     */
    public BlockPlacedEvent(Type type, Block theBlock) {
        super(type, theBlock);
        cancel = false;
    }
    
    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}

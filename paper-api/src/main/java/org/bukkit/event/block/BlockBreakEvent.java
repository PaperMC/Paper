package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 *
 * @author Meaglin
 */
public class BlockBreakEvent extends BlockEvent implements Cancellable {

    private Player player;
    private boolean cancel;

    public BlockBreakEvent(final Block theBlock, Player player) {
        super(Type.BLOCK_BREAK, theBlock);
        this.player = player;
        this.cancel = false;
    }

    /**
     * Returns the player doing the damage
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}

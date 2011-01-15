package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.BlockDamageLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * @author tkelly
 */
public class BlockDamageEvent extends BlockEvent implements Cancellable {
    private Player player;
    private BlockDamageLevel damageLevel;
    private boolean cancel;

    public BlockDamageEvent(Type type, Block block, BlockDamageLevel level, Player player) {
        super(type, block);
        this.damageLevel = level;
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

    /**
     * Returns the level of damage to the block
     * 
     * @return
     */
    public BlockDamageLevel getDamageLevel() {
        return damageLevel;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}

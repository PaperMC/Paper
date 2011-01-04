package org.bukkit.event.player;

import org.bukkit.Block;
import org.bukkit.ItemStack;
import org.bukkit.Player;
import org.bukkit.event.Cancellable;

/**
 * 
 * @author durron597
 *
 */
public class PlayerItemUseEvent extends PlayerEvent implements Cancellable {
    protected ItemStack item;
    protected Block blockClicked;
    protected boolean cancel;
    
    public PlayerItemUseEvent(Type type, Player who, ItemStack item, Block blockClicked) {
        super(type, who);
        this.item = item;
        this.blockClicked = blockClicked;
        cancel = false;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you
     * want to prevent buckets from placing water and so forth
     * 
     * @return boolean cancellation state
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}

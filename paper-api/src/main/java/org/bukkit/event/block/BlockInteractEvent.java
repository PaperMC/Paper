package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * This event is triggered whenever an entity interacts with the universe
 * it's always called, on a left click or a right click, or walking on
 * (as in the case of pressure plates). Use cancellable to prevent things
 * from happening (doors opening, buttons, pressure plates being walked
 * on, etc). Note: even though pressure plates work totally differently
 * than the other interact events, it's still thrown in with this event. 
 * 
 * @author durron597
 */
public class BlockInteractEvent extends BlockEvent implements Cancellable {
    protected boolean cancel;
    protected LivingEntity theEntity;
    
    /**
     * @param type The type of this event
     * @param interactedBlock the block that was interacted with
     * @param who The entity that interacted with 
     */
    public BlockInteractEvent(Type type, Block interactedBlock, LivingEntity who) {
        super(type, interactedBlock);
        theEntity = who;
        cancel = false;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    /**
     * Returns the entity that triggered this event
     * 
     * @return Entity the entity that triggered this event
     */
    public LivingEntity getEntity() {
        return theEntity;
    }
    
    /**
     * Convenience method for seeing if this event was triggered by a player
     * 
     * @return boolean whether this event was triggered by a player
     */
    public boolean isPlayer() {
       return theEntity instanceof Player; 
    }
}

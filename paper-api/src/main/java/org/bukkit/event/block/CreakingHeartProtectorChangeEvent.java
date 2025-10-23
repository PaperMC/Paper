package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;


/**
 * Called when the Creaking Heart block attempts to spawn or remove its protector (Creaking mob).
 * <p>
 * Examples:
 * <ul>
 *   <li>Protector is about to spawn near the Creaking Heart block.
 *   <li>Protector is about to be removed (due to death, distance, or other reasons).
 * </ul>
 * <p>
 */
@NullMarked
public class CreakingHeartProtectorChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    
    public boolean spawn = false;
    @Nullable
    private final LivingEntity protector;
    private boolean cancelled;

    /**
     * Constructs a new CreakingHeartProtectorChangeEvent.
     *
     * @param block the Creaking Heart block involved in this event
     * @param protector the protector entity (may be null for REMOVE if not available)
     * @param spawn the indicator of whether the mob is spawned or not
     */
    @ApiStatus.Internal
    public CreakingHeartProtectorChangeEvent(Block block, @Nullable LivingEntity protector, boolean spawn) {
        super(block);
        this.protector = protector;
        this.spawn = spawn;
    }

    /**
     * Gets the protector entity involved in this event.
     *
     * @return the protector entity, or null if not available
     */
    @Nullable
    public LivingEntity getProtector() {
        return protector;
    }

    /**
     * Get whether the protector is being spawned.
     *
     * @return whether the protector is being spawned
     */
    public boolean isSpawned() {
        return spawn;
    }

    @Override
    public boolean isCancelled() {
        return cancelled; 
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel; 
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST; 
    }

    public static HandlerList getHandlerList() { 
        return HANDLER_LIST; 
    }
}

package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when the Creaking Heart block attempts to spawn or remove its protector (Creaking mob).
 * <p>
 * Examples:
 * <ul>
 *   <li>Protector is about to spawn near the Creaking Heart block.
 *   <li>Protector is about to be removed (due to death, distance, or other reasons).
 * </ul>
 * <p>
 * If this event is cancelled, the spawn or removal of the protector will not occur.
 */
public class CreakingHeartProtectorChangeEvent extends BlockEvent implements Cancellable {
    /**
     * The type of action being performed on the protector.
     */
    public enum Action { SPAWN, REMOVE }

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Action action;
    @Nullable
    private final LivingEntity protector;
    private boolean cancelled;

    /**
     * Constructs a new CreakingHeartProtectorChangeEvent.
     *
     * @param block the Creaking Heart block involved in this event
     * @param protector the protector entity (may be null for REMOVE if not available)
     * @param action the action being performed (SPAWN or REMOVE)
     */
    public CreakingHeartProtectorChangeEvent(@NotNull Block block, @Nullable LivingEntity protector, @NotNull Action action) {
        super(block);
        this.protector = protector;
        this.action = action;
    }

    /**
     * Gets the protector entity involved in this event.
     *
     * @return the protector entity, or null if not available
     */
    @Nullable
    public LivingEntity getProtector() { return protector; }

    /**
     * Gets the action being performed (SPAWN or REMOVE).
     *
     * @return the action
     */
    @NotNull
    public Action getAction() {
        return action;
 }

    @Override
    public boolean isCancelled() {
        return cancelled; 
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel; 
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST; 
    }

    @NotNull
    public static HandlerList getHandlerList() { 
        return HANDLER_LIST; 
    }
}

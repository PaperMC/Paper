package org.bukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an EnderDragon switches controller phase.
 */
public class EnderDragonChangePhaseEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final EnderDragon.Phase currentPhase;
    private EnderDragon.Phase newPhase;

    private boolean cancelled;

    @ApiStatus.Internal
    public EnderDragonChangePhaseEvent(@NotNull EnderDragon enderDragon, @Nullable EnderDragon.Phase currentPhase, @NotNull EnderDragon.Phase newPhase) {
        super(enderDragon);
        this.currentPhase = currentPhase;
        this.newPhase = newPhase;
    }

    @NotNull
    @Override
    public EnderDragon getEntity() {
        return (EnderDragon) this.entity;
    }

    /**
     * Gets the current phase that the dragon is in. This method will return null
     * when a dragon is first spawned and hasn't yet been assigned a phase.
     *
     * @return the current dragon phase
     */
    @Nullable
    public EnderDragon.Phase getCurrentPhase() {
        return this.currentPhase;
    }

    /**
     * Gets the new phase that the dragon will switch to.
     *
     * @return the new dragon phase
     */
    @NotNull
    public EnderDragon.Phase getNewPhase() {
        return this.newPhase;
    }

    /**
     * Sets the new phase for the ender dragon.
     *
     * @param newPhase the new dragon phase
     */
    public void setNewPhase(@NotNull EnderDragon.Phase newPhase) {
        Preconditions.checkArgument(newPhase != null, "New dragon phase cannot be null");
        this.newPhase = newPhase;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
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

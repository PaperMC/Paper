package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Armadillo;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an armadillo's state changes.
 * <p>
 * If the event is cancelled, the armadillo's state will not change.
 */
@NullMarked
public class ArmadilloStateChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Armadillo.State oldState;
    private Armadillo.State newState;
    private boolean cancelled;

    @ApiStatus.Internal
    public ArmadilloStateChangeEvent(final Armadillo armadillo, final Armadillo.State oldState, final Armadillo.State newState) {
        super(armadillo);
        this.oldState = oldState;
        this.newState = newState;
    }

    /**
     * Sets the new state for the armadillo.
     * <p>
     * You can modify the outcome of the state change by setting a different state.
     *
     * @param newState the new state to set, must not be null
     */
    public void setNewState(final Armadillo.State newState) {
        Preconditions.checkNotNull(newState, "newState cannot be null");
        this.newState = newState;
    }

    /**
     * Gets the old state of the armadillo.
     *
     * @return the previous armadillo state
     */
    public Armadillo.State getOldState() {
        return this.oldState;
    }

    /**
     * Gets the new state that the armadillo is transitioning to.
     *
     * @return the new armadillo state
     */
    public Armadillo.State getNewState() {
        return this.newState;
    }

    @Override
    public Armadillo getEntity() {
        return (Armadillo) super.getEntity();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
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

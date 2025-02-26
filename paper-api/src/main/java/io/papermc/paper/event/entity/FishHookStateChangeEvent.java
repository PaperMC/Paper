package io.papermc.paper.event.entity;

import org.bukkit.entity.FishHook;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called just before a {@link FishHook}'s {@link FishHook.HookState} is changed.
 *
 * <p>If you want to monitor a player's fishing state transitions, you can listen to them using {@link PlayerFishEvent}.</p>
 */
@NullMarked
public final class FishHookStateChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final FishHook.HookState newHookState;
    private boolean cancelled;

    @ApiStatus.Internal
    public FishHookStateChangeEvent(final FishHook entity, final FishHook.HookState newHookState) {
        super(entity);
        this.newHookState = newHookState;
    }

    /**
     * Get the <strong>new</strong> hook state of the {@link FishHook}.
     *
     * <p>This is what the {@link FishHook}'s new hook state will be after this event if it isn't cancelled.<br>
     * Refer to {@link FishHook#getState()} to get the current hook state.</p>
     *
     * @return the <strong>new</strong> hook state
     */
    public FishHook.HookState getNewHookState() {
        return this.newHookState;
    }

    @Override
    public FishHook getEntity() {
        return (FishHook) super.getEntity();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Set whether to cancel the {@link FishHook}'s {@link FishHook.HookState} change.
     *
     * <p>Note: Even if you cancel this event, if the conditions for the state change are met in the next tick,
     * the event will be called again.<br>
     * In other words, this event may be triggered repeatedly every tick, so use it with caution.</p>
     *
     * @param cancel {@code true} if you wish to cancel the state change
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}

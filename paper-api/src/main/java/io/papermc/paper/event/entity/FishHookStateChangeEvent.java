package io.papermc.paper.event.entity;

import org.bukkit.entity.FishHook;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Called just before a {@link FishHook}'s {@link FishHook.HookState} is changed.
 * <p>
 * If you want to monitor a player's fishing state transition, you can use {@link PlayerFishEvent}.
 */
public interface FishHookStateChangeEvent extends EntityEventNew {

    /**
     * Get the <strong>new</strong> hook state of the {@link FishHook}.
     * <p>
     * Refer to {@link FishHook#getState()} to get the current hook state.
     *
     * @return the <strong>new</strong> hook state
     */
    FishHook.HookState getNewHookState();

    @Override
    FishHook getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

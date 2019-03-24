package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Sent when an entity's swimming status is toggled.
 */
public class EntityToggleSwimEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;
    private final boolean isSwimming;

    public EntityToggleSwimEvent(@NotNull LivingEntity who, final boolean isSwimming) {
        super(who);
        this.isSwimming = isSwimming;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * @deprecated This does nothing, the server and the client doesn't work
     * correctly when the server try to bypass this. A current workaround
     * exists. If you want to cancel the switch from the ground state to the
     * swimming state you need to disable the sprinting flag for the player after
     * the cancel action.
     */
    @Deprecated // Paper
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Returns true if the entity is now swims or
     * false if the entity stops swimming.
     *
     * @return new swimming state
     */
    public boolean isSwimming() {
        return isSwimming;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}

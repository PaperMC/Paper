package io.papermc.paper.event.entity;

import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Is called when an {@link ElderGuardian} appears in front of a {@link Player}.
 *
 * @since 1.16.5
 */
@NullMarked
public class ElderGuardianAppearanceEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player affectedPlayer;
    private boolean cancelled;

    @ApiStatus.Internal
    public ElderGuardianAppearanceEvent(final ElderGuardian guardian, final Player affectedPlayer) {
        super(guardian);
        this.affectedPlayer = affectedPlayer;
    }

    /**
     * Get the player affected by the guardian appearance.
     *
     * @return Player affected by the appearance
     */
    public Player getAffectedPlayer() {
        return this.affectedPlayer;
    }

    /**
     * The elder guardian playing the effect.
     *
     * @return The elder guardian
     */
    @Override
    public ElderGuardian getEntity() {
        return (ElderGuardian) super.getEntity();
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

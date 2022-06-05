package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player gets kicked from the server
 */
public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String leaveMessage;
    private String kickReason;
    private boolean cancel;

    public PlayerKickEvent(@NotNull final Player playerKicked, @NotNull final String kickReason, @NotNull final String leaveMessage) {
        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cancel = false;
    }

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     */
    @NotNull
    public String getReason() {
        return kickReason;
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     */
    @NotNull
    public String getLeaveMessage() {
        return leaveMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     */
    public void setReason(@NotNull String kickReason) {
        this.kickReason = kickReason;
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     */
    public void setLeaveMessage(@NotNull String leaveMessage) {
        this.leaveMessage = leaveMessage;
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

package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private String leaveMessage;
    private String kickReason;
    private Boolean cancel;

    public PlayerKickEvent(Player playerKicked, String kickReason, String leaveMessage) {
        super(Type.PLAYER_KICK, playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cancel = false;
    }

    /**
     * Gets the cancellation state of this event. Set to true if you
     * want to prevent the player from getting kicked.
     *
     * @return boolean cancellation state
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     */
    public String getReason() {
        return kickReason;
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     */
    public String getLeaveMessage() {
        return leaveMessage;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * Cancelling this event will prevent the kick of the targetted player
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     */
    public void setReason(String kickReason) {
        this.kickReason = kickReason;
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     */
    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}

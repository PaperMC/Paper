package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player gets kicked from the server
 */
public interface PlayerKickEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     */
    Component reason();

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     */
    void reason(Component kickReason);

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     * @deprecated in favour of {@link #reason()}
     */
    @Deprecated
    String getReason();

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     * @deprecated in favour of {@link #reason(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    void setReason(String kickReason);

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     */
    Component leaveMessage();

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     */
    void leaveMessage(Component leaveMessage);

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     * @deprecated in favour of {@link #leaveMessage()}
     */
    @Deprecated
    String getLeaveMessage();

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     * @deprecated in favour of {@link #leaveMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    void setLeaveMessage(String leaveMessage);

    /**
     * Gets the cause of this kick
     */
    Cause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {

        PLUGIN,
        WHITELIST,
        BANNED,
        IP_BANNED,
        KICK_COMMAND,
        FLYING_PLAYER,
        FLYING_VEHICLE,
        TIMEOUT,
        IDLING,
        INVALID_VEHICLE_MOVEMENT,
        INVALID_PLAYER_MOVEMENT,
        INVALID_ENTITY_ATTACKED,
        INVALID_PAYLOAD,
        INVALID_COOKIE,
        SPAM,
        ILLEGAL_ACTION,
        ILLEGAL_CHARACTERS,
        OUT_OF_ORDER_CHAT,
        UNSIGNED_CHAT,
        CHAT_VALIDATION_FAILED,
        EXPIRED_PROFILE_PUBLIC_KEY,
        INVALID_PUBLIC_KEY_SIGNATURE,
        TOO_MANY_PENDING_CHATS,
        SELF_INTERACTION,
        DUPLICATE_LOGIN,
        RESOURCE_PACK_REJECTION,
        /**
         * Spigot's restart command
         */
        RESTART_COMMAND,
        /**
         * Fallback cause
         */
        UNKNOWN,
    }
}

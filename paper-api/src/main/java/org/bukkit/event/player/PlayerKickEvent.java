package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a player gets kicked from the server
 */
@NullMarked
public class PlayerKickEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Component kickReason;
    private Component leaveMessage;
    private final Cause cause;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerKickEvent(final Player playerKicked, final String kickReason, final String leaveMessage) {
        super(playerKicked);
        this.kickReason = LegacyComponentSerializer.legacySection().deserialize(kickReason);
        this.leaveMessage = LegacyComponentSerializer.legacySection().deserialize(leaveMessage);
        this.cause  = Cause.UNKNOWN;
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerKickEvent(final Player playerKicked, final Component kickReason, final Component leaveMessage) {
        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cause = Cause.UNKNOWN;
    }

    @ApiStatus.Internal
    public PlayerKickEvent(final Player playerKicked, final Component kickReason, final Component leaveMessage, final Cause cause) {
        super(playerKicked);
        this.kickReason = kickReason;
        this.leaveMessage = leaveMessage;
        this.cause = cause;
    }

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     */
    public Component reason() {
        return this.kickReason;
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     */
    public void reason(Component kickReason) {
        this.kickReason = kickReason;
    }

    /**
     * Gets the reason why the player is getting kicked
     *
     * @return string kick reason
     * @deprecated in favour of {@link #reason()}
     */
    @Deprecated
    public String getReason() {
        return LegacyComponentSerializer.legacySection().serialize(this.kickReason);
    }

    /**
     * Sets the reason why the player is getting kicked
     *
     * @param kickReason kick reason
     * @deprecated in favour of {@link #reason(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void setReason(String kickReason) {
        this.kickReason = LegacyComponentSerializer.legacySection().deserialize(kickReason);
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     */
    public @Nullable Component leaveMessage() {
        return this.leaveMessage;
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message. If {@code null}, no message will be sent
     */
    public void leaveMessage(@Nullable Component leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    /**
     * Gets the leave message send to all online players
     *
     * @return string kick reason
     * @deprecated in favour of {@link #leaveMessage()}
     */
    @Deprecated
    public String getLeaveMessage() {
        return LegacyComponentSerializer.legacySection().serialize(this.leaveMessage);
    }

    /**
     * Sets the leave message send to all online players
     *
     * @param leaveMessage leave message
     * @deprecated in favour of {@link #leaveMessage(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = LegacyComponentSerializer.legacySection().deserialize(leaveMessage);
    }

    /**
     * Gets the cause of this kick
     */
    public PlayerKickEvent.Cause getCause() {
        return this.cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Cause {

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

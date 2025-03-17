package io.papermc.paper.event.connection.common;

import io.papermc.paper.connection.PlayerConnection;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Validates whether a player connection would be able to login at this event.
 * <p>
 * Currently, this occurs when the player connection is attempting to log in for the first time, or is finishing up
 * being configured.
 */
// TODO: I am not sure about this event however. I have seen the most common reasons for needing a LoginEvent is to prevent the fullness check. Should we add a separate event for each case? Like WhitelistVerifyEvent?
public class PlayerConnectionValidateLoginEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final PlayerConnection connection;
    private Result result;
    private Component message;

    @ApiStatus.Internal
    public PlayerConnectionValidateLoginEvent(PlayerConnection connection, final Result result, final Component message) {
        super(false);
        this.connection = connection;
        this.result = result;
        this.message = message;
    }

    public PlayerConnection getConnection() {
        return connection;
    }

    public Component getMessage() {
        return message;
    }

    public Result getResult() {
        return result;
    }

    /**
     * Allows the player to log in
     */
    public void allow() {
        result = Result.ALLOWED;
        message = net.kyori.adventure.text.Component.empty();
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(@NotNull final Result result, @NotNull final net.kyori.adventure.text.Component message) {
        this.result = result;
        this.message = message;
    }

    /**
     * Basic kick reasons for communicating to plugins
     */
    public enum Result {

        /**
         * The player is allowed to log in
         */
        ALLOWED,
        /**
         * The player is not allowed to log in, due to the server being full
         */
        KICK_FULL,
        /**
         * The player is not allowed to log in, due to them being banned
         */
        KICK_BANNED,
        /**
         * The player is not allowed to log in, due to them not being on the
         * white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER
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

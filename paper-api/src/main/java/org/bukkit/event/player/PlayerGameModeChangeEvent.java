package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when the GameMode of the player is changed.
 * <p>
 * <b>NOTE:</b> When {@link #getCause()} is {@link Cause#DEFAULT_GAMEMODE},
 * the Player from {@link #getPlayer()} might not be fully online at
 * the time this event is fired. Plugins should use {@link Player#isOnline()}
 * to check before changing player state.
 */
public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final GameMode newGameMode;
    private final Cause cause;
    private Component cancelMessage;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerGameModeChangeEvent(@NotNull final Player player, @NotNull final GameMode newGameMode) {
        this(player, newGameMode, Cause.UNKNOWN, null);
    }

    @ApiStatus.Internal
    public PlayerGameModeChangeEvent(@NotNull final Player player, @NotNull final GameMode newGameMode, @NotNull Cause cause, @org.jetbrains.annotations.Nullable net.kyori.adventure.text.Component cancelMessage) {
        super(player);
        this.newGameMode = newGameMode;
        this.cause = cause;
        this.cancelMessage = cancelMessage;
    }

    /**
     * Gets the GameMode the player is switched to.
     *
     * @return  player's new GameMode
     */
    @NotNull
    public GameMode getNewGameMode() {
        return this.newGameMode;
    }

    /**
     * Gets the cause of this gamemode change.
     *
     * @return the cause
     */
    @NotNull
    public Cause getCause() {
        return this.cause;
    }

    /**
     * <b>Only valid if the cause of the gamemode change was directly due to a command.</b>.
     * Gets the message shown to the command user if the event is cancelled
     * as a notification that a player's gamemode was not changed.
     * <p>
     * This returns {@code null} if the gamemode change was due to a plugin, or a
     * player joining the game with a gamemode not equal to the server default gamemode
     * and {@code force-gamemode} is set to {@code true}.
     *
     * @return the error message shown to the command user, {@code null} if not directly caused by a command
     */
    @Nullable
    public Component cancelMessage() {
        return this.cancelMessage;
    }

    /**
     * Sets the message shown to the command user if the event was cancelled.
     * <b>The message is only shown to cancelled events that are directly called by a command
     * not by a plugin or a player joining with the wrong gamemode.</b>
     *
     * @param message the error message shown to the command user, {@code null} to show no message.
     */
    public void cancelMessage(@Nullable Component message) {
        this.cancelMessage = message;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Cause {

        /**
         * A plugin changed the player's gamemode with
         * {@link Player#setGameMode(GameMode)}.
         */
        PLUGIN,
        /**
         * The {@code /gamemode} command was used.
         */
        COMMAND,
        /**
         * A player had their gamemode changed as a result of
         * the {@code /defaultgamemode} command, or they joined
         * with a gamemode that was not the default gamemode and
         * {@code force-gamemode} in {@code server.properties} is set to {@code true}.
         */
        DEFAULT_GAMEMODE,
        /**
         * When the player dies in a hardcore world and has their gamemode
         * changed to {@link GameMode#SPECTATOR}.
         */
        HARDCORE_DEATH,
        /**
         * This cause is only used if a plugin fired their own
         * {@link PlayerGameModeChangeEvent} and did not include a
         * cause. Can usually be ignored.
         */
        UNKNOWN
    }
}

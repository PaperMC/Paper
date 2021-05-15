package org.bukkit.event.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the GameMode of the player is changed.
 * <p>
 * <b>NOTE:</b> When {@link #getCause()} is {@link Cause#DEFAULT_GAMEMODE},
 * the Player from {@link #getPlayer()} might not be fully online at
 * the time this event is fired. Plugins should use {@link Player#isOnline()}
 * to check before changing player state.
 */
public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final GameMode newGameMode;
    // Paper start
    private final Cause cause;
    private net.kyori.adventure.text.Component cancelMessage;

    @Deprecated // Paper end
    public PlayerGameModeChangeEvent(@NotNull final Player player, @NotNull final GameMode newGameMode) {
        // Paper start
        this(player, newGameMode, Cause.UNKNOWN, null);
    }

    public PlayerGameModeChangeEvent(@NotNull final Player player, @NotNull final GameMode newGameMode, @NotNull Cause cause, @org.jetbrains.annotations.Nullable net.kyori.adventure.text.Component cancelMessage) {
        // Paper end
        super(player);
        this.newGameMode = newGameMode;
        this.cause = cause; // Paper
        this.cancelMessage = cancelMessage; // Paper
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the GameMode the player is switched to.
     *
     * @return  player's new GameMode
     */
    @NotNull
    public GameMode getNewGameMode() {
        return newGameMode;
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
    // Paper start
    /**
     * Gets the cause of this gamemode change.
     *
     * @return the cause
     */
    @NotNull
    public Cause getCause() {
        return cause;
    }

    /**
     * <b>Only valid if the cause of the gamemode change was directly due to a command.</b>.
     * Gets the message shown to the command user if the event is cancelled
     * as a notification that a player's gamemode was not changed.
     * <p>
     * This returns {@code null} if the gamemode change was due to a plugin, or a
     * player joining the game with a gamemode not equal to the server default gamemode
     * and {@code force-gamemode} is set to true.
     *
     * @return the error message shown to the command user, null if not directly caused by a command
     */
    @org.jetbrains.annotations.Nullable
    public net.kyori.adventure.text.Component cancelMessage() {
        return cancelMessage;
    }

    /**
     * Sets the message shown to the command user if the event was cancelled.
     * <b>The message is only shown to cancelled events that are directly called by a command
     * not by a plugin or a player joining with the wrong gamemode.</b>
     *
     * @param message the error message shown to the command user, null to show no message.
     */
    public void cancelMessage(@org.jetbrains.annotations.Nullable net.kyori.adventure.text.Component message) {
        this.cancelMessage = message;
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
         * {@code force-gamemode} in {@code server.properties} is set to true.
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
        UNKNOWN,
    }
    // Paper end
}

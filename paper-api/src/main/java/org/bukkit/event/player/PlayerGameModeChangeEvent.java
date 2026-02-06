package org.bukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when the GameMode of the player is changed.
 * <p>
 * <b>NOTE:</b> When {@link #getCause()} is {@link Cause#DEFAULT_GAMEMODE},
 * the Player from {@link #getPlayer()} might not be fully online at
 * the time this event is fired. Plugins should use {@link Player#isOnline()}
 * to check before changing player state.
 */
public interface PlayerGameModeChangeEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the GameMode the player is switched to.
     *
     * @return  player's new GameMode
     */
    GameMode getNewGameMode();

    /**
     * Gets the cause of this gamemode change.
     *
     * @return the cause
     */
    Cause getCause();

    /**
     * <b>Only valid if the gamemode change was caused by the {@code /gamemode} command or the gamemode switcher.</b>
     * Gets the message shown to the command user if the event is cancelled
     * as a notification that a player's gamemode was not changed.
     *
     * @return the error message shown to the command user, {@code null} by default
     */
    @Nullable Component cancelMessage();

    /**
     * Sets the message shown to the command user if the event was cancelled.
     * <b>The message is only shown to cancelled events that are called by the {@code /gamemode} command
     * or the gamemode switcher.</b>
     *
     * @param message the error message shown to the command user, {@code null} to show no message.
     */
    void cancelMessage(@Nullable Component message);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum Cause {

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
         * A player changed their gamemode using the gamemode switcher (F3+F4)
         * or spectator hotkey (F3+N).
         */
        GAMEMODE_SWITCHER,
        /**
         * This cause is only used if a plugin fired their own
         * {@link PlayerGameModeChangeEvent} and did not include a
         * cause. Can usually be ignored.
         */
        UNKNOWN
    }
}

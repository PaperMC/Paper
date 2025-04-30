package io.papermc.paper.event.server;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when the default gamemode is changed, either by command or by api.
 */
@NullMarked
public class DefaultGameModeChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable CommandSender commandSender;
    private GameMode newGameMode;

    @ApiStatus.Internal
    public DefaultGameModeChangeEvent(final @Nullable CommandSender commandSender, final GameMode mode) {
        this.commandSender = commandSender;
        this.newGameMode = mode;
    }

    /**
     * Gets the command sender associated with this event.
     *
     * @return {@code null} if the default gamemode was changed via api, otherwise the {@link CommandSender}.
     */
    public @Nullable CommandSender getCommandSender() {
        return commandSender;
    }

    /**
     * Gets the new default gamemode.
     *
     * @return the new default gamemode.
     */
    public GameMode getNewGameMode() {
        return newGameMode;
    }

    /**
     * Sets the new default gamemode.
     *
     * @param mode the new default gamemode.
     */
    public DefaultGameModeChangeEvent setNewGameMode(final GameMode mode) {
        this.newGameMode = mode;
        return this;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

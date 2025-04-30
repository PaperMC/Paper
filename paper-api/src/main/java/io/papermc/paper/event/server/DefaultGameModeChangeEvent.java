package io.papermc.paper.event.server;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class DefaultGameModeChangeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @Nullable CommandSender commandSender;
    private GameMode newGameMode;

    @ApiStatus.Internal
    public DefaultGameModeChangeEvent(final @Nullable CommandSender commandSender, final GameMode newGameMode) {
        this.commandSender = commandSender;
        this.newGameMode = newGameMode;
    }

    public @Nullable CommandSender getCommandSender() {
        return commandSender;
    }

    public GameMode getNewGameMode() {
        return newGameMode;
    }

    public DefaultGameModeChangeEvent setNewGameMode(final GameMode newGameMode) {
        this.newGameMode = newGameMode;
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

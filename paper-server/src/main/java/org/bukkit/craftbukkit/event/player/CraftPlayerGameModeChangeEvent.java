package org.bukkit.craftbukkit.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.jspecify.annotations.Nullable;

public class CraftPlayerGameModeChangeEvent extends CraftPlayerEvent implements PlayerGameModeChangeEvent {

    private final GameMode newGameMode;
    private final Cause cause;
    private @Nullable Component cancelMessage;

    private boolean cancelled;

    public CraftPlayerGameModeChangeEvent(final Player player, final GameMode newGameMode, final Cause cause, final @Nullable Component cancelMessage) {
        super(player);
        this.newGameMode = newGameMode;
        this.cause = cause;
        this.cancelMessage = cancelMessage;
    }

    @Override
    public GameMode getNewGameMode() {
        return this.newGameMode;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public @Nullable Component cancelMessage() {
        return this.cancelMessage;
    }

    @Override
    public void cancelMessage(final @Nullable Component message) {
        this.cancelMessage = message;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerGameModeChangeEvent.getHandlerList();
    }
}

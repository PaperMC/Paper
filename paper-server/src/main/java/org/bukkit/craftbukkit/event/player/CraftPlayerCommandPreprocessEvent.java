package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CraftPlayerCommandPreprocessEvent extends CraftPlayerEvent implements PlayerCommandPreprocessEvent {

    private Player mutablePlayer;
    private String message;
    private final Set<Player> recipients;

    private boolean cancelled;

    public CraftPlayerCommandPreprocessEvent(final Player player, final String message, final Set<Player> recipients) {
        super(player);
        this.mutablePlayer = player;
        this.recipients = recipients;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String command) throws IllegalArgumentException {
        Preconditions.checkArgument(command != null, "Command cannot be null");
        Preconditions.checkArgument(!command.isEmpty(), "Command cannot be empty");
        this.message = command;
    }

    @Deprecated(since = "1.3.1", forRemoval = true)
    public Set<Player> getRecipients() {
        return this.recipients;
    }

    @Override
    public Player getPlayer() {
        return this.mutablePlayer;
    }

    @Deprecated(forRemoval = true)
    public void setPlayer(final Player player) throws IllegalArgumentException {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        this.mutablePlayer = player;
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
        return PlayerCommandPreprocessEvent.getHandlerList();
    }
}

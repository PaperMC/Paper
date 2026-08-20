package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChatEvent;

public class CraftPlayerChatEvent extends CraftPlayerEvent implements PlayerChatEvent {

    private final Set<Player> recipients;
    private Player mutablePlayer;
    private String message;
    private String format;

    private boolean cancelled;

    public CraftPlayerChatEvent(final Player player, final String message, final String format, final Set<Player> recipients) {
        super(player);
        this.mutablePlayer = player;
        this.message = message;
        this.format = format;
        this.recipients = recipients;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(final String message) {
        this.message = message;
    }

    @Override
    public Player getPlayer() {
        return this.mutablePlayer;
    }

    @Override
    public void setPlayer(final Player player) {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        this.mutablePlayer = player;
    }

    @Override
    public String getFormat() {
        return this.format;
    }

    @Override
    public void setFormat(final String format) {
        // Oh for a better way to do this!
        try {
            String.format(format, this.mutablePlayer, this.message);
        } catch (RuntimeException ex) {
            ex.fillInStackTrace();
            throw ex;
        }

        this.format = format;
    }

    @Override
    public Set<Player> getRecipients() {
        return this.recipients;
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
        return PlayerChatEvent.getHandlerList();
    }
}

package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CraftAsyncPlayerChatEvent extends CraftPlayerEvent implements AsyncPlayerChatEvent {

    private String message;
    private String format = "<%1$s> %2$s";
    private final Set<Player> recipients;

    private boolean cancelled;

    public CraftAsyncPlayerChatEvent(final boolean async, final Player player, final String message, final Set<Player> players) {
        super(async, player);
        this.message = message;
        this.recipients = players;
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
    public String getFormat() {
        return this.format;
    }

    @Override
    public void setFormat(final String format) {
        Preconditions.checkArgument(format != null, "format cannot be null");
        // Oh for a better way to do this!
        try {
            String.format(format, this.player, this.message);
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
        return AsyncPlayerChatEvent.getHandlerList();
    }
}

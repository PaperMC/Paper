package io.papermc.paper.event.server;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerException;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperServerExceptionEvent extends CraftEvent implements ServerExceptionEvent {

    private final ServerException exception;

    public PaperServerExceptionEvent(final ServerException exception) {
        super(!Bukkit.isPrimaryThread());
        this.exception = exception;
    }

    @Override
    public ServerException getException() {
        return this.exception;
    }

    @Override
    public HandlerList getHandlers() {
        return ServerExceptionEvent.getHandlerList();
    }
}

package io.papermc.paper.event.server;

import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.craftbukkit.event.server.CraftServerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperAsyncChatDecorateEvent extends CraftServerEvent implements AsyncChatDecorateEvent {

    private final @Nullable Player player;
    private final Component originalMessage;
    private Component result;

    private boolean cancelled;

    public PaperAsyncChatDecorateEvent(final @Nullable Player player, final Component originalMessage) {
        super(true);
        this.player = player;
        this.originalMessage = originalMessage;
        this.result = originalMessage;
    }

    @Override
    public @Nullable Player player() {
        return this.player;
    }

    @Override
    public Component originalMessage() {
        return this.originalMessage;
    }

    @Override
    public Component result() {
        return this.result;
    }

    @Override
    public void result(final Component result) {
        this.result = result;
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
        return AsyncChatDecorateEvent.getHandlerList();
    }
}

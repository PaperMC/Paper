package io.papermc.paper.event.server;

import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.Experimental
public class PaperAsyncChatCommandDecorateEvent extends PaperAsyncChatDecorateEvent implements AsyncChatCommandDecorateEvent {

    public PaperAsyncChatCommandDecorateEvent(final @Nullable Player player, final Component originalMessage) {
        super(player, originalMessage);
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncChatCommandDecorateEvent.getHandlerList();
    }
}

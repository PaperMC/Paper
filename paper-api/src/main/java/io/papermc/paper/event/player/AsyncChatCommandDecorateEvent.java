package io.papermc.paper.event.player;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface AsyncChatCommandDecorateEvent extends AsyncChatDecorateEvent {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

package io.papermc.paper.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public interface TestEvent extends Event {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

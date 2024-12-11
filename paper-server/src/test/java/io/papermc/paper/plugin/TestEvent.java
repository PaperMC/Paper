package io.papermc.paper.plugin;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TestEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public TestEvent(boolean async) {
        super(async);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

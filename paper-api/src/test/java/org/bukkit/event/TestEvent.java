package org.bukkit.event;


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

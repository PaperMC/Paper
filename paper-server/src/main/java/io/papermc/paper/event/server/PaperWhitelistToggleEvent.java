package io.papermc.paper.event.server;

import com.destroystokyo.paper.event.server.WhitelistToggleEvent;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperWhitelistToggleEvent extends CraftEvent implements WhitelistToggleEvent {

    private final boolean enabled;

    public PaperWhitelistToggleEvent(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public HandlerList getHandlers() {
        return WhitelistToggleEvent.getHandlerList();
    }
}

package io.papermc.paper.event.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperPlayerServerFullCheckEvent extends CraftEvent implements PlayerServerFullCheckEvent {

    private final PlayerProfile profile;
    private Component kickMessage;
    private boolean allow;

    public PaperPlayerServerFullCheckEvent(final PlayerProfile profile, final Component kickMessage, final boolean allow) {
        this.profile = profile;
        this.kickMessage = kickMessage;
        this.allow = allow;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public Component kickMessage() {
        return this.kickMessage;
    }

    @Override
    public void deny(final Component kickMessage) {
        this.kickMessage = kickMessage;
        this.allow = false;
    }

    @Override
    public void allow(final boolean allow) {
        this.allow = allow;
    }

    @Override
    public boolean isAllowed() {
        return this.allow;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerServerFullCheckEvent.getHandlerList();
    }
}

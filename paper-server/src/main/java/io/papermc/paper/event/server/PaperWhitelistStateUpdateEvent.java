package io.papermc.paper.event.server;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperWhitelistStateUpdateEvent extends CraftEvent implements WhitelistStateUpdateEvent {

    private final PlayerProfile playerProfile;
    private final WhitelistStatus status;

    private boolean cancelled;

    public PaperWhitelistStateUpdateEvent(final PlayerProfile playerProfile, final WhitelistStatus status) {
        this.playerProfile = playerProfile;
        this.status = status;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.playerProfile.getId());
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    @Override
    public WhitelistStatus getStatus() {
        return this.status;
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
        return WhitelistStateUpdateEvent.getHandlerList();
    }
}

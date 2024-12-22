package io.papermc.paper.event.server;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * This event gets called when the whitelist status of a player is changed
 *
 * @since 1.20.1
 */
@NullMarked
public class WhitelistStateUpdateEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerProfile playerProfile;
    private final WhitelistStatus status;

    private boolean cancelled;

    @ApiStatus.Internal
    public WhitelistStateUpdateEvent(final PlayerProfile playerProfile, final WhitelistStatus status) {
        this.playerProfile = playerProfile;
        this.status = status;
    }

    /**
     * Gets the player whose whitelist status is being changed
     *
     * @return the player whose status is being changed
     */
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.playerProfile.getId());
    }

    /**
     * Gets the player profile whose whitelist status is being changed
     *
     * @return the player profile whose status is being changed
     */
    public PlayerProfile getPlayerProfile() {
        return this.playerProfile;
    }

    /**
     * Gets the status change of the player profile
     *
     * @return the whitelist status
     */
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Enum for the whitelist status changes
     */
    public enum WhitelistStatus {
        ADDED, REMOVED
    }
}

package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Allows a plugin to be notified anytime AFTER a Profile has been looked up from the Mojang API
 * This is an opportunity to view the response and potentially cache things.
 * <p>
 * No guarantees are made about thread execution context for this event. If you need to know, check
 * {@link Event#isAsynchronous()}
 */
@NullMarked
public class LookupProfileEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerProfile profile;

    @ApiStatus.Internal
    public LookupProfileEvent(final PlayerProfile profile) {
        super(!Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    /**
     * @return The profile that was recently looked up. This profile can be mutated
     */
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

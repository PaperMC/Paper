package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Allows a plugin to be notified anytime AFTER a Profile has been looked up from the Mojang API
 * This is an opportunity to view the response and potentially cache things.
 * <p>
 * No guarantees are made about thread execution context for this event. If you need to know, check
 * {@link Event#isAsynchronous()}
 */
public interface LookupProfileEvent extends Event {

    /**
     * Gets the name of the profile that was looked up.
     *
     * @return the name of the profile
     */
    String getName();

    /**
     * Gets the UUID of the profile that was looked up.
     *
     * @return the UUID of the profile
     */
    UUID getId();

    /**
     * @return The profile that was recently looked up. This profile can be mutated
     * @deprecated This event is only called after UUID lookups, properties set here will be ignored. Use {@link FillProfileEvent} for setting properties.
     */
    @Deprecated(forRemoval = true, since = "1.21.9")
    PlayerProfile getPlayerProfile();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

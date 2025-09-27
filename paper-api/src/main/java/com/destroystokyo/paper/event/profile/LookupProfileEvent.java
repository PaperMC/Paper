package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import java.util.Objects;
import java.util.UUID;

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
     * Gets the name of the profile that was looked up.
     *
     * @return the name of the profile
     */
    public String getName() {
        return Objects.requireNonNull(this.profile.getName(), "profile name");
    }

    /**
     * Gets the UUID of the profile that was looked up.
     *
     * @return the UUID of the profile
     */
    public UUID getId() {
        return Objects.requireNonNull(this.profile.getId(), "profile id");
    }

    /**
     * @return The profile that was recently looked up. This profile can be mutated
     * @deprecated This event is only called after UUID lookups, properties set here will be ignored. Use {@link FillProfileEvent} for setting properties.
     */
    @Deprecated(forRemoval = true, since = "1.21.9")
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

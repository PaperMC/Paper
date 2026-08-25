package com.destroystokyo.paper.event.profile;

import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Allows a plugin to intercept a Profile Lookup for a Profile by name
 * <p>
 * At the point of event fire, the UUID and properties are unset.
 * <p>
 * If a plugin sets the UUID, and optionally the properties, the API call to look up the profile may be skipped.
 * <p>
 * No guarantees are made about thread execution context for this event. If you need to know, check
 * {@link Event#isAsynchronous()}
 */
public interface PreLookupProfileEvent extends Event {

    /**
     * @return Name of the profile
     */
    String getName();

    /**
     * If this value is left {@code null} by the completion of the event call, then the server will
     * trigger a call to the Mojang API to look up the UUID (Network Request), and subsequently, fire a
     * {@link LookupProfileEvent}
     *
     * @return The UUID of the profile if it has already been provided by a plugin
     */
    @Nullable UUID getUUID();

    /**
     * Sets the UUID for this player name. This will skip the initial API call to find the players UUID.
     * <p>
     * However, if Profile Properties are needed by the server, you must also set them or else an API call might still be made.
     *
     * @param uuid the UUID to set for the profile or {@code null} to reset
     */
    void setUUID(@Nullable UUID uuid);

    /**
     * @return The currently pending pre-populated properties.
     * Any property in this Set will be automatically prefilled on this Profile
     * @deprecated This event is only called for UUID lookups, properties set here will be ignored. Use {@link PreFillProfileEvent} for setting properties.
     */
    @Deprecated(forRemoval = true, since = "1.21.9")
    Set<ProfileProperty> getProfileProperties();

    /**
     * Clears any existing pre-populated properties and uses the supplied properties
     * Any property in this Set will be automatically prefilled on this Profile
     *
     * @param properties The properties to add
     * @deprecated This event is only called for UUID lookups, properties set here will be ignored. Use {@link PreFillProfileEvent} for setting properties.
     */
    @Deprecated(forRemoval = true, since = "1.21.9")
    void setProfileProperties(Set<ProfileProperty> properties);

    /**
     * Adds any properties currently missing to the pre-populated properties set, replacing any that already were set. Use {@link PreFillProfileEvent} for setting properties.
     * Any property in this Set will be automatically prefilled on this Profile
     *
     * @param properties The properties to add
     * @deprecated This event is only called for UUID lookups, properties set here will be ignored.
     */
    @Deprecated(forRemoval = true, since = "1.21.9")
    void addProfileProperties(Set<ProfileProperty> properties);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

package org.bukkit.ban;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.profile.PlayerProfile;
import org.jspecify.annotations.Nullable;

/**
 * A {@link BanList} targeting player profile bans.
 */
public interface ProfileBanList extends BanList<com.destroystokyo.paper.profile.PlayerProfile> {

    /**
     * Adds a ban to this list. If a previous ban exists, this will
     * update the previous entry.
     *
     * @param target  the target of the ban
     * @param reason  reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *                forever
     * @param source  source of the ban, null indicates implementation default
     * @param <E>     the type of the ban entry
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     * @throws IllegalArgumentException if ProfilePlayer has an invalid UUID
     * @deprecated use {@link #addBan(com.destroystokyo.paper.profile.PlayerProfile, String, Date, String)}
     */
    @Deprecated
    <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> @Nullable E addBan(
        PlayerProfile target,
        @Nullable String reason,
        @Nullable Date expires,
        @Nullable String source
    );

    /**
     * Adds a ban to this list. If a previous ban exists, this will
     * update the previous entry.
     *
     * @param target  the target of the ban
     * @param reason  reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *                forever
     * @param source  source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     * @throws IllegalArgumentException if ProfilePlayer has an invalid UUID
     */
    @Override
    @Nullable BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(
        com.destroystokyo.paper.profile.PlayerProfile target,
        @Nullable String reason,
        @Nullable Date expires,
        @Nullable String source
    );

    // the 5 methods below are added to maintain compat for the bukkit.PlayerProfile parameter type

    /**
     * Gets a {@link BanEntry} by target.
     *
     * @param target entry parameter to search for
     * @param <E>    the type of the ban entry
     * @return the corresponding entry, or null if none found
     * @deprecated use {@link #getBanEntry(Object)}
     */
    @Deprecated
    <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> @Nullable E getBanEntry(PlayerProfile target);

    /**
     * Gets if a {@link BanEntry} exists for the target, indicating an active
     * ban status.
     *
     * @param target the target to find
     * @return true if a {@link BanEntry} exists for the target, indicating an
     *     active ban status, false otherwise
     * @deprecated use {@link #isBanned(Object)}
     */
    @Deprecated
    boolean isBanned(PlayerProfile target);

    /**
     * Removes the specified target from this list, therefore indicating a
     * "not banned" status.
     *
     * @param target the target to remove from this list
     * @deprecated use {@link #pardon(Object)}
     */
    @Deprecated
    void pardon(PlayerProfile target);

    /**
     * Adds a ban to this list. If a previous ban exists, this will
     * update the previous entry.
     *
     * @param target  the target of the ban
     * @param reason  reason for the ban, null indicates implementation default
     * @param expires instant for the ban's expiration (unban), or null to imply
     *                forever
     * @param source  source of the ban, null indicates implementation default
     * @param <E>     the type of the ban entry
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     * @deprecated use {@link #addBan(Object, String, Instant, String)}
     */
    @Deprecated
    <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> @Nullable E addBan(
        PlayerProfile target,
        @Nullable String reason,
        @Nullable Instant expires,
        @Nullable String source
    );

    /**
     * Adds a ban to this list. If a previous ban exists, this will
     * update the previous entry.
     *
     * @param target   the target of the ban
     * @param reason   reason for the ban, null indicates implementation default
     * @param duration the duration of the ban, or null to imply forever
     * @param source   source of the ban, null indicates implementation default
     * @param <E>      the type of the ban entry
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     * @deprecated use {@link #addBan(Object, String, Duration, String)}
     */
    @Deprecated
    <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> @Nullable E addBan(
        PlayerProfile target,
        @Nullable String reason,
        @Nullable Duration duration,
        @Nullable String source
    );
}

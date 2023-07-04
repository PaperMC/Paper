package org.bukkit.ban;

import java.util.Date;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link BanList} targeting player profile bans.
 */
public interface ProfileBanList extends BanList<com.destroystokyo.paper.profile.PlayerProfile> { // Paper

    /**
     * {@inheritDoc}
     *
     * @param target the target of the ban
     * @param reason reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     * @throws IllegalArgumentException if ProfilePlayer has an invalid UUID
     * @deprecated use {@link #addBan(com.destroystokyo.paper.profile.PlayerProfile, String, Date, String)}
     */
    @Nullable
    // Paper start
    @Deprecated
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E addBan(@NotNull PlayerProfile target, @Nullable String reason, @Nullable Date expires, @Nullable String source);

    /**
     * @throws IllegalArgumentException if ProfilePlayer has an invalid UUID
     */
    @Nullable BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(com.destroystokyo.paper.profile.@NotNull PlayerProfile target, @Nullable String reason, @Nullable Date expires, @Nullable String source);

    // the 5 methods below are added to maintain compat for the bukkit.PlayerProfile parameter type
    /**
     * @deprecated use {@link #getBanEntry(Object)}
     */
    @Deprecated
    @Nullable <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E getBanEntry(@NotNull PlayerProfile target);

    /**
     * @deprecated use {@link #isBanned(Object)}
     */
    @Deprecated
    boolean isBanned(@NotNull PlayerProfile target);

    /**
     * @deprecated use {@link #pardon(Object)}
     */
    @Deprecated
    void pardon(@NotNull PlayerProfile target);

    /**
     * @deprecated use {@link #addBan(Object, String, java.time.Instant, String)}
     */
    @Deprecated
    @Nullable <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E addBan(@NotNull PlayerProfile target, @Nullable String reason, @Nullable java.time.Instant expires, @Nullable String source);

    /**
     * @deprecated use {@link #addBan(Object, String, java.time.Duration, String)}
     */
    @Deprecated
    @Nullable <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E addBan(@NotNull PlayerProfile target, @Nullable String reason, @Nullable java.time.Duration duration, @Nullable String source);

    // Paper end
}

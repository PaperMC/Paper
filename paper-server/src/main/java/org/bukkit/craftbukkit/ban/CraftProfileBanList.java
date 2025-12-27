package org.bukkit.craftbukkit.ban;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import org.bukkit.BanEntry;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.profile.PlayerProfile;

public class CraftProfileBanList implements ProfileBanList {
    private final UserBanList list;

    public CraftProfileBanList(UserBanList list) {
        this.list = list;
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> getBanEntry(String target) { // Paper
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.getBanEntry(CraftProfileBanList.getProfile(target));
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> getBanEntry(PlayerProfile target) { // Paper
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.getBanEntry(new NameAndId(((com.destroystokyo.paper.profile.SharedPlayerProfile) target).buildGameProfile())); // Paper
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> getBanEntry(final com.destroystokyo.paper.profile.PlayerProfile target) {
        Preconditions.checkArgument(target != null, "target cannot be null");

        return this.getBanEntry(new NameAndId(((com.destroystokyo.paper.profile.SharedPlayerProfile) target).buildGameProfile()));
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(final com.destroystokyo.paper.profile.PlayerProfile target, final String reason, final Date expires, final String source) {
        Preconditions.checkArgument(target != null, "PlayerProfile cannot be null");
        Preconditions.checkArgument(target.getId() != null, "The PlayerProfile UUID cannot be null");

        return this.addBan(new NameAndId(((com.destroystokyo.paper.profile.SharedPlayerProfile) target).buildGameProfile()), reason, expires, source);
    }

    @Override
    public boolean isBanned(final com.destroystokyo.paper.profile.PlayerProfile target) {
        return this.isBanned((com.destroystokyo.paper.profile.SharedPlayerProfile) target);
    }

    @Override
    public void pardon(final com.destroystokyo.paper.profile.PlayerProfile target) {
        this.pardon((com.destroystokyo.paper.profile.SharedPlayerProfile) target);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(final com.destroystokyo.paper.profile.PlayerProfile target, final String reason, final Instant expires, final String source) {
        Date date = expires != null ? Date.from(expires) : null;
        return this.addBan(target, reason, date, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(final com.destroystokyo.paper.profile.PlayerProfile target, final String reason, final Duration duration, final String source) {
        Instant instant = duration != null ? Instant.now().plus(duration) : null;
        return this.addBan(target, reason, instant, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(String target, String reason, Date expires, String source) { // Paper - fix ban list API
        Preconditions.checkArgument(target != null, "Ban target cannot be null");

        return this.addBan(CraftProfileBanList.getProfileByName(target), reason, expires, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(PlayerProfile target, String reason, Date expires, String source) { // Paper - fix ban list API
        Preconditions.checkArgument(target != null, "PlayerProfile cannot be null");
        Preconditions.checkArgument(target.getUniqueId() != null, "The PlayerProfile UUID cannot be null");

        return this.addBan(new NameAndId(((com.destroystokyo.paper.profile.SharedPlayerProfile) target).buildGameProfile()), reason, expires, source); // Paper
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(PlayerProfile target, String reason, Instant expires, String source) { // Paper - fix ban list API
        Date date = expires != null ? Date.from(expires) : null;
        return this.addBan(target, reason, date, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(PlayerProfile target, String reason, Duration duration, String source) { // Paper - fix ban list API
        Instant instant = duration != null ? Instant.now().plus(duration) : null;
        return this.addBan(target, reason, instant, source);
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        ImmutableSet.Builder<BanEntry> builder = ImmutableSet.builder();
        for (UserBanListEntry entry : this.list.getEntries()) {
            NameAndId profile = entry.getUser();
            builder.add(new CraftProfileBanEntry(profile, entry, this.list));
        }

        return builder.build();
    }

    @Override
    public Set<BanEntry<com.destroystokyo.paper.profile.PlayerProfile>> getEntries() { // Paper
        ImmutableSet.Builder<BanEntry<com.destroystokyo.paper.profile.PlayerProfile>> builder = ImmutableSet.builder(); // Paper
        for (UserBanListEntry entry : this.list.getEntries()) {
            NameAndId profile = entry.getUser();
            builder.add(new CraftProfileBanEntry(profile, entry, this.list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(PlayerProfile target) {
        return this.isBanned((com.destroystokyo.paper.profile.SharedPlayerProfile) target);
    }

    private boolean isBanned(com.destroystokyo.paper.profile.SharedPlayerProfile target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.isBanned(new NameAndId(target.buildGameProfile())); // Paper
    }

    @Override
    public boolean isBanned(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.isBanned(CraftProfileBanList.getProfile(target));
    }

    @Override
    public void pardon(PlayerProfile target) {
        this.pardon((com.destroystokyo.paper.profile.SharedPlayerProfile) target);
    }

    private void pardon(com.destroystokyo.paper.profile.SharedPlayerProfile target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        this.pardon(new NameAndId(target.buildGameProfile())); // Paper
    }

    @Override
    public void pardon(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        this.pardon(CraftProfileBanList.getProfile(target));
    }

    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> getBanEntry(NameAndId profile) { // Paper
        if (profile == null) {
            return null;
        }

        UserBanListEntry entry = this.list.get(profile);
        if (entry == null) {
            return null;
        }

        return new CraftProfileBanEntry(profile, entry, this.list);
    }

    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> addBan(NameAndId profile, String reason, Date expires, String source) { // Paper
        if (profile == null) {
            return null;
        }

        UserBanListEntry entry = new UserBanListEntry(profile, new Date(),
                (source == null || source.isBlank()) ? null : source, expires,
                (reason == null || reason.isBlank()) ? null : reason);

        this.list.add(entry);

        return new CraftProfileBanEntry(profile, entry, this.list);
    }

    private void pardon(NameAndId profile) {
        this.list.remove(profile);
    }

    private boolean isBanned(NameAndId profile) {
        return profile != null && this.list.isBanned(profile);
    }

    static NameAndId getProfile(String target) {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(target);
        } catch (IllegalArgumentException ignored) {
        }

        return (uuid != null) ? CraftProfileBanList.getProfileByUUID(uuid) : CraftProfileBanList.getProfileByName(target);
    }

    static NameAndId getProfileByUUID(UUID uuid) {
        return (MinecraftServer.getServer() != null) ? MinecraftServer.getServer().services().nameToIdCache().get(uuid).orElse(null) : null;
    }

    static NameAndId getProfileByName(String name) {
        return (MinecraftServer.getServer() != null) ? MinecraftServer.getServer().services().nameToIdCache().get(name).orElse(null) : null;
    }
}

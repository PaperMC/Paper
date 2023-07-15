package org.bukkit.craftbukkit.ban;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileBanEntry;
import net.minecraft.server.players.GameProfileBanList;
import org.bukkit.BanEntry;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.profile.PlayerProfile;

public class CraftProfileBanList implements ProfileBanList {
    private final GameProfileBanList list;

    public CraftProfileBanList(GameProfileBanList list) {
        this.list = list;
    }

    @Override
    public BanEntry<PlayerProfile> getBanEntry(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.getBanEntry(getProfile(target));
    }

    @Override
    public BanEntry<PlayerProfile> getBanEntry(PlayerProfile target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.getBanEntry(((CraftPlayerProfile) target).buildGameProfile());
    }

    @Override
    public BanEntry<PlayerProfile> addBan(String target, String reason, Date expires, String source) {
        Preconditions.checkArgument(target != null, "Ban target cannot be null");

        return this.addBan(getProfileByName(target), reason, expires, source);
    }

    @Override
    public BanEntry<PlayerProfile> addBan(PlayerProfile target, String reason, Date expires, String source) {
        Preconditions.checkArgument(target != null, "PlayerProfile cannot be null");
        Preconditions.checkArgument(target.getUniqueId() != null, "The PlayerProfile UUID cannot be null");

        return this.addBan(((CraftPlayerProfile) target).buildGameProfile(), reason, expires, source);
    }

    @Override
    public BanEntry<PlayerProfile> addBan(PlayerProfile target, String reason, Instant expires, String source) {
        Date date = expires != null ? Date.from(expires) : null;
        return addBan(target, reason, date, source);
    }

    @Override
    public BanEntry<PlayerProfile> addBan(PlayerProfile target, String reason, Duration duration, String source) {
        Instant instant = duration != null ? Instant.now().plus(duration) : null;
        return addBan(target, reason, instant, source);
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        ImmutableSet.Builder<BanEntry> builder = ImmutableSet.builder();
        for (GameProfileBanEntry entry : list.getEntries()) {
            GameProfile profile = entry.getUser();
            builder.add(new CraftProfileBanEntry(profile, entry, list));
        }

        return builder.build();
    }

    @Override
    public Set<BanEntry<PlayerProfile>> getEntries() {
        ImmutableSet.Builder<BanEntry<PlayerProfile>> builder = ImmutableSet.builder();
        for (GameProfileBanEntry entry : list.getEntries()) {
            GameProfile profile = entry.getUser();
            builder.add(new CraftProfileBanEntry(profile, entry, list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(PlayerProfile target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.isBanned(((CraftPlayerProfile) target).buildGameProfile());
    }

    @Override
    public boolean isBanned(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        return this.isBanned(getProfile(target));
    }

    @Override
    public void pardon(PlayerProfile target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        this.pardon(((CraftPlayerProfile) target).buildGameProfile());
    }

    @Override
    public void pardon(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        this.pardon(getProfile(target));
    }

    public BanEntry<PlayerProfile> getBanEntry(GameProfile profile) {
        if (profile == null) {
            return null;
        }

        GameProfileBanEntry entry = list.get(profile);
        if (entry == null) {
            return null;
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    public BanEntry<PlayerProfile> addBan(GameProfile profile, String reason, Date expires, String source) {
        if (profile == null) {
            return null;
        }

        GameProfileBanEntry entry = new GameProfileBanEntry(profile, new Date(),
                (source == null || source.isBlank()) ? null : source, expires,
                (reason == null || reason.isBlank()) ? null : reason);

        list.add(entry);

        return new CraftProfileBanEntry(profile, entry, list);
    }

    private void pardon(GameProfile profile) {
        list.remove(profile);
    }

    private boolean isBanned(GameProfile profile) {
        return profile != null && list.isBanned(profile);
    }

    static GameProfile getProfile(String target) {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(target);
        } catch (IllegalArgumentException ignored) {
        }

        return (uuid != null) ? getProfileByUUID(uuid) : getProfileByName(target);
    }

    static GameProfile getProfileByUUID(UUID uuid) {
        return (MinecraftServer.getServer() != null) ? MinecraftServer.getServer().getProfileCache().get(uuid).orElse(null) : null;
    }

    static GameProfile getProfileByName(String name) {
        return (MinecraftServer.getServer() != null) ? MinecraftServer.getServer().getProfileCache().get(name).orElse(null) : null;
    }
}

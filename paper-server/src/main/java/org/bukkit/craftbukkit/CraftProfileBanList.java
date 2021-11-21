package org.bukkit.craftbukkit;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileBanEntry;
import net.minecraft.server.players.GameProfileBanList;
import net.minecraft.server.players.JsonListEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

public class CraftProfileBanList implements org.bukkit.BanList {
    private final GameProfileBanList list;

    public CraftProfileBanList(GameProfileBanList list) {
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = getProfile(target);
        if (profile == null) {
            return null;
        }

        GameProfileBanEntry entry = (GameProfileBanEntry) list.get(profile);
        if (entry == null) {
            return null;
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public org.bukkit.BanEntry addBan(String target, String reason, Date expires, String source) {
        Validate.notNull(target, "Ban target cannot be null");

        GameProfile profile = getProfile(target);
        if (profile == null) {
            return null;
        }

        GameProfileBanEntry entry = new GameProfileBanEntry(profile, new Date(),
                StringUtils.isBlank(source) ? null : source, expires,
                StringUtils.isBlank(reason) ? null : reason);

        list.add(entry);

        try {
            list.save();
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save banned-players.json, {0}", ex.getMessage());
        }

        return new CraftProfileBanEntry(profile, entry, list);
    }

    @Override
    public Set<org.bukkit.BanEntry> getBanEntries() {
        ImmutableSet.Builder<org.bukkit.BanEntry> builder = ImmutableSet.builder();

        for (JsonListEntry entry : list.getValues()) {
            GameProfile profile = (GameProfile) entry.getUser();
            builder.add(new CraftProfileBanEntry(profile, (GameProfileBanEntry) entry, list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = getProfile(target);
        if (profile == null) {
            return false;
        }

        return list.isBanned(profile);
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = getProfile(target);
        list.remove(profile);
    }

    private GameProfile getProfile(String target) {
        UUID uuid = null;

        try {
            uuid = UUID.fromString(target);
        } catch (IllegalArgumentException ex) {
            //
        }

        return ((uuid != null) ? MinecraftServer.getServer().getProfileCache().get(uuid) : MinecraftServer.getServer().getProfileCache().get(target)).orElse(null);
    }
}

package org.bukkit.craftbukkit;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import net.minecraft.server.GameProfileBanEntry;
import net.minecraft.server.GameProfileBanList;
import net.minecraft.server.JsonListEntry;
import net.minecraft.server.MinecraftServer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class CraftProfileBanList implements org.bukkit.BanList {
    private final GameProfileBanList list;

    public CraftProfileBanList(GameProfileBanList list){
        this.list = list;
    }

    @Override
    public org.bukkit.BanEntry getBanEntry(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
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

        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
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
            GameProfile profile = (GameProfile) entry.getKey();
            builder.add(new CraftProfileBanEntry(profile, (GameProfileBanEntry) entry, list));
        }

        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
        if (profile == null) {
            return false;
        }

        return list.isBanned(profile);
    }

    @Override
    public void pardon(String target) {
        Validate.notNull(target, "Target cannot be null");

        GameProfile profile = MinecraftServer.getServer().getUserCache().getProfile(target);
        list.remove(profile);
    }
}

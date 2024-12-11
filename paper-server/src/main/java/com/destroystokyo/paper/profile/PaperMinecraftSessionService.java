package com.destroystokyo.paper.profile;

import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import java.net.Proxy;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class PaperMinecraftSessionService extends YggdrasilMinecraftSessionService {

    protected PaperMinecraftSessionService(ServicesKeySet servicesKeySet, Proxy proxy, Environment environment) {
        super(servicesKeySet, proxy, environment);
    }

    public @Nullable ProfileResult fetchProfile(GameProfile profile, final boolean requireSecure) {
        CraftPlayerProfile playerProfile = (CraftPlayerProfile) CraftPlayerProfile.asBukkitMirror(profile);
        new com.destroystokyo.paper.event.profile.PreFillProfileEvent(playerProfile).callEvent();
        profile = playerProfile.getGameProfile();
        if (profile.getProperties().containsKey("textures")) {
            return new ProfileResult(profile, java.util.Collections.emptySet());
        }
        ProfileResult result = super.fetchProfile(profile.getId(), requireSecure);
        if (result != null) {
            new com.destroystokyo.paper.event.profile.FillProfileEvent(CraftPlayerProfile.asBukkitMirror(result.profile())).callEvent();
        }
        return result;
    }

    @Override @io.papermc.paper.annotation.DoNotUse @Deprecated
    public @Nullable ProfileResult fetchProfile(final UUID profileId, final boolean requireSecure) {
        return super.fetchProfile(profileId, requireSecure);
    }
}

package com.destroystokyo.paper.profile;

import com.destroystokyo.paper.event.profile.FillProfileEvent;
import com.destroystokyo.paper.event.profile.PreFillProfileEvent;
import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import java.net.Proxy;
import java.util.Collections;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class PaperMinecraftSessionService extends YggdrasilMinecraftSessionService {

    protected PaperMinecraftSessionService(ServicesKeySet servicesKeySet, Proxy proxy, Environment environment) {
        super(servicesKeySet, proxy, environment);
    }

    public @Nullable ProfileResult fetchProfile(GameProfile profile, final boolean requireSecure) {
        CraftPlayerProfile playerProfile = (CraftPlayerProfile) CraftPlayerProfile.asBukkitCopy(profile);
        new PreFillProfileEvent(playerProfile).callEvent();
        profile = playerProfile.getGameProfile();
        if (profile.properties().containsKey("textures")) {
            return new ProfileResult(profile, Collections.emptySet());
        }
        ProfileResult result = super.fetchProfile(profile.id(), requireSecure);
        if (result != null) {
            final FillProfileEvent event = new FillProfileEvent(CraftPlayerProfile.asBukkitCopy(result.profile()));
            event.callEvent();
            result = new ProfileResult(CraftPlayerProfile.asAuthlibCopy(event.getPlayerProfile()), result.actions());
        }
        return result;
    }

    @Override @Deprecated
    public @Nullable ProfileResult fetchProfile(final UUID profileId, final boolean requireSecure) {
        return super.fetchProfile(profileId, requireSecure);
    }
}

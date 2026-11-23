package com.destroystokyo.paper.profile;

import com.destroystokyo.paper.event.profile.FillProfileEvent;
import com.destroystokyo.paper.event.profile.PreFillProfileEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.services.MinecraftServicesSessionService;
import com.mojang.authlib.services.ProfileResult;
import com.mojang.authlib.services.ServicesKeySet;
import java.net.Proxy;
import java.util.Collections;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class PaperMinecraftSessionService extends MinecraftServicesSessionService {

    private static final boolean ENFORCE_UUID_V4 = !Boolean.getBoolean("paper.disableProfileUuidVersionCheck");

    protected PaperMinecraftSessionService(ServicesKeySet servicesKeySet, Proxy proxy, PaperServicesDiscoveryService discoveryService) {
        super(servicesKeySet, proxy, discoveryService);
    }

    public @Nullable ProfileResult fetchProfile(GameProfile profile, final boolean requireSecure) {
        CraftPlayerProfile playerProfile = (CraftPlayerProfile) CraftPlayerProfile.asBukkitCopy(profile);
        new PreFillProfileEvent(playerProfile).callEvent();
        profile = playerProfile.getGameProfile();
        if (profile.properties().containsKey("textures")) {
            return new ProfileResult(profile, Collections.emptySet());
        }
        if (ENFORCE_UUID_V4 && (profile.id() == null || profile.id().version() != 4)) {
            return null;
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
        if (ENFORCE_UUID_V4 && (profileId == null || profileId.version() != 4)) {
            return null;
        }
        return super.fetchProfile(profileId, requireSecure);
    }
}

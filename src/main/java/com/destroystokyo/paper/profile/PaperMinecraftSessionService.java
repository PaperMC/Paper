package com.destroystokyo.paper.profile;

import com.mojang.authlib.Environment;
import com.destroystokyo.paper.event.profile.FillProfileEvent;
import com.destroystokyo.paper.event.profile.PreFillProfileEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import java.util.Map;

public class PaperMinecraftSessionService extends YggdrasilMinecraftSessionService {
    protected PaperMinecraftSessionService(YggdrasilAuthenticationService authenticationService, Environment environment) {
        super(authenticationService, environment);
    }

    @Override
    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(GameProfile profile, boolean requireSecure) {
        return super.getTextures(profile, requireSecure);
    }

    @Override
    public GameProfile fillProfileProperties(GameProfile profile, boolean requireSecure) {
        CraftPlayerProfile playerProfile = (CraftPlayerProfile) CraftPlayerProfile.asBukkitMirror(profile);
        new PreFillProfileEvent(playerProfile).callEvent();
        profile = playerProfile.getGameProfile();
        if (profile.isComplete() && profile.getProperties().containsKey("textures")) {
            return profile;
        }
        GameProfile gameProfile = super.fillProfileProperties(profile, requireSecure);
        new FillProfileEvent(CraftPlayerProfile.asBukkitMirror(gameProfile)).callEvent();
        return gameProfile;
    }

    @Override
    protected GameProfile fillGameProfile(GameProfile profile, boolean requireSecure) {
        return super.fillGameProfile(profile, requireSecure);
    }
}

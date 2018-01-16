package com.destroystokyo.paper.profile;

import com.mojang.authlib.Environment;
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

    @Override
    public @Nullable ProfileResult fetchProfile(final UUID profileId, final boolean requireSecure) {
        return super.fetchProfile(profileId, requireSecure);
    }
}

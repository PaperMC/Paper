package com.destroystokyo.paper.profile;

import com.mojang.authlib.Environment;
import com.mojang.authlib.EnvironmentParser;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;

import java.net.Proxy;

public class PaperAuthenticationService extends YggdrasilAuthenticationService {

    private final Environment environment;

    public PaperAuthenticationService(Proxy proxy) {
        super(proxy);
        this.environment = EnvironmentParser.getEnvironmentFromProperties().orElse(YggdrasilEnvironment.PROD.getEnvironment());
    }

    @Override
    public MinecraftSessionService createMinecraftSessionService() {
        return new PaperMinecraftSessionService(this.getServicesKeySet(),  this.getProxy(), this.environment);
    }

    @Override
    public GameProfileRepository createProfileRepository() {
        return new PaperGameProfileRepository(this.getProxy(), this.environment);
    }
}

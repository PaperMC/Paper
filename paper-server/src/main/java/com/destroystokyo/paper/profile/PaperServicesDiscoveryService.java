package com.destroystokyo.paper.profile;

import com.mojang.authlib.Environment;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.SessionService;
import com.mojang.authlib.services.MinecraftServicesDiscoveryService;
import com.mojang.authlib.services.response.discovery.DiscoveryResponse;
import java.net.Proxy;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaperServicesDiscoveryService extends MinecraftServicesDiscoveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaperServicesDiscoveryService.class);

    public PaperServicesDiscoveryService(final Proxy proxy, final boolean servicesKeySetEnabled, final Supplier<DiscoveryResponse> discoverySupplier) {
        super(proxy, servicesKeySetEnabled, discoverySupplier);
    }

    public static PaperServicesDiscoveryService create(final Proxy proxy) {
        return create(proxy, true, determineEnvironment());
    }

    public static PaperServicesDiscoveryService create(final Proxy proxy, final boolean servicesKeySetEnabled) {
        return create(proxy, servicesKeySetEnabled, determineEnvironment());
    }

    public static PaperServicesDiscoveryService create(
        final Proxy proxy,
        final boolean servicesKeySetEnabled,
        final Environment environment
    ) {
        LOGGER.info("Environment: {}", environment);
        return new PaperServicesDiscoveryService(
            proxy,
            servicesKeySetEnabled,
            createDiscoverySupplier(proxy, environment)
        );
    }

    @Override
    public SessionService createMinecraftSessionService() {
        return new PaperMinecraftSessionService(getServicesKeySet(), getProxy(), this);
    }

    @Override
    public GameProfileRepository createProfileRepository() {
        return new PaperGameProfileRepository(getProxy(), this);
    }
}

package io.papermc.paper.configuration;

import org.spigotmc.SpigotConfig;

public class PaperServerConfiguration implements ServerConfiguration {
    @Override
    public boolean isProxyOnlineMode() {
        return GlobalConfiguration.get().proxies.isProxyOnlineMode();
    }

    @Override
    public boolean isVelocityEnabled() {
        return GlobalConfiguration.get().proxies.velocity.enabled;
    }

    @Override
    public boolean isBungeeCordEnabled() {
        return SpigotConfig.bungee;
    }

    @Override
    public boolean isVelocityOnlineMode() {
        return isVelocityEnabled() && GlobalConfiguration.get().proxies.velocity.onlineMode;
    }

    @Override
    public boolean isBungeeCordOnlineMode() {
        return isBungeeCordEnabled() && GlobalConfiguration.get().proxies.bungeeCord.onlineMode;
    }
}

package io.papermc.paper.configuration;

import org.spigotmc.SpigotConfig;

public class PaperServerConfiguration implements ServerConfiguration {

    @Override
    public boolean isProxyOnlineMode() {
        return GlobalConfiguration.get().proxies.isProxyOnlineMode();
    }

    @Override
    public boolean isProxyEnabled() {
        return GlobalConfiguration.get().proxies.velocity.enabled || SpigotConfig.bungee;
    }
}

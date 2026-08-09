package io.papermc.paper.configuration;


public class PaperServerConfiguration implements ServerConfiguration {

    @Override
    public boolean isProxyOnlineMode() {
        return GlobalConfiguration.get().proxies.isProxyOnlineMode();
    }

    @Override
    public boolean isProxyEnabled() {
        return GlobalConfiguration.get().proxies.velocity.enabled || io.papermc.paper.configuration.GlobalConfiguration.get().proxies.bungeeCord.enabled;
    }
}

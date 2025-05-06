package io.papermc.paper.configuration;

public class PaperServerConfiguration implements ServerConfiguration {

    @Override
    public boolean isProxyOnlineMode() {
        return GlobalConfiguration.get().proxies.isProxyOnlineMode();
    }
}

package io.papermc.paper.plugin.entrypoint.dependency;

import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;

public interface DependencyContextHolder {

    void setContext(DependencyContext context);

}

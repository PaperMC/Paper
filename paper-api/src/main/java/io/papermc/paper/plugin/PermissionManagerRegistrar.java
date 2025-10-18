package io.papermc.paper.plugin;

import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;

public interface PermissionManagerRegistrar extends Registrar {

    void register(PermissionManager permissionManager);

}

package io.papermc.paper.plugin;

import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface PermissionManagerRegistrar extends Registrar {

    void register(PermissionManager permissionManager);

}

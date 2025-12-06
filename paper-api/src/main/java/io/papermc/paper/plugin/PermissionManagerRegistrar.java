package io.papermc.paper.plugin;

import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface PermissionManagerRegistrar extends Registrar {

    void register(@NotNull PermissionManager permissionManager);

}

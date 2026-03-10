package io.papermc.paper.plugin.manager;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.PermissionManager;
import io.papermc.paper.plugin.PermissionManagerRegistrar;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.PaperRegistrar;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

@NullMarked
public enum PermissionRegistrar implements PermissionManagerRegistrar, PaperRegistrar<BootstrapContext> {
    INSTANCE;

    private static final Logger LOGGER = LogUtils.getClassLogger();

    private BootstrapContext owner;

    private @Nullable PermissionManager registered;
    private String registeredOwner;

    @Override
    public void register(PermissionManager permissionManager) {
        if (this.registered != null) {
            LOGGER.warn("Permission manager registration overridden! (Was {}, now is {})", this.registeredOwner, this.owner.getPluginMeta().getDisplayName());
        }

        this.registered = permissionManager;
        this.registeredOwner = this.owner.getPluginMeta().getDisplayName();
    }

    @Override
    public void setCurrentContext(@Nullable BootstrapContext owner) {
        this.owner = owner;
    }

    public @Nullable PermissionManager getRegistered() {
        return this.registered;
    }

    public String getRegisteredOwner() {
        return registeredOwner;
    }
}

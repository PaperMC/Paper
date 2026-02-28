package io.papermc.testplugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.PermissionManager;
import io.papermc.paper.plugin.PermissionManagerRegistrar;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.testplugin.brigtests.example.MaterialArgumentType;
import org.bukkit.Material;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.PERMISSION_MANAGER_REGISTER, event -> {
            final PermissionManagerRegistrar permissionManagerRegistrar = event.registrar();

            PermissionManager permissionManager = new PermissionManager() {
                @Override
                public @org.jspecify.annotations.Nullable Permission getPermission(String name) {
                    return null;
                }

                @Override
                public void addPermission(Permission perm) {

                }

                @Override
                public void removePermission(Permission perm) {

                }

                @Override
                public void removePermission(String name) {

                }

                @Override
                public Set<Permission> getDefaultPermissions(boolean op) {
                    return Set.of();
                }

                @Override
                public void recalculatePermissionDefaults(Permission perm) {

                }

                @Override
                public void subscribeToPermission(String permission, Permissible permissible) {

                }

                @Override
                public void unsubscribeFromPermission(String permission, Permissible permissible) {

                }

                @Override
                public Set<Permissible> getPermissionSubscriptions(String permission) {
                    return Set.of();
                }

                @Override
                public void subscribeToDefaultPerms(boolean op, Permissible permissible) {

                }

                @Override
                public void unsubscribeFromDefaultPerms(boolean op, Permissible permissible) {

                }

                @Override
                public Set<Permissible> getDefaultPermSubscriptions(boolean op) {
                    return Set.of();
                }

                @Override
                public Set<Permission> getPermissions() {
                    return Set.of();
                }

                @Override
                public void addPermissions(List<Permission> perm) {

                }

                @Override
                public void clearPermissions() {

                }

                @Override
                public Permissible createPermissible(@NotNull ServerOperator operator) {
                    return EmptyPermissible.INSTANCE;
                }

                @Override
                public Permissible createCommandBlockPermissible() {
                    return EmptyPermissible.INSTANCE;
                }

                @Override
                public CompletableFuture<Optional<Permissible>> loadPlayerPermissible(@NotNull UUID playerUuid) {
                    return CompletableFuture.completedFuture(Optional.of(EmptyPermissible.INSTANCE));
                }
            };
            permissionManagerRegistrar.register(permissionManager);
        });
    }

    enum EmptyPermissible implements Permissible {
        INSTANCE;

        @Override
        public boolean isPermissionSet(@NotNull final String name) {
            return true;
        }

        @Override
        public boolean isPermissionSet(@NotNull final Permission perm) {
            return true;
        }

        @Override
        public boolean hasPermission(@NotNull final String name) {
            return true;
        }

        @Override
        public boolean hasPermission(@NotNull final Permission perm) {
            return true;
        }

        @Override
        public @NotNull PermissionAttachment addAttachment(@NotNull final Plugin plugin, @NotNull final String name, final boolean value) {
            throw new UnsupportedOperationException("Expected a read only instance!");
        }

        @Override
        public @NotNull PermissionAttachment addAttachment(@NotNull final Plugin plugin) {
            throw new UnsupportedOperationException("Expected a read only instance!");
        }

        @Override
        public @Nullable PermissionAttachment addAttachment(@NotNull final Plugin plugin, @NotNull final String name, final boolean value, final int ticks) {
            throw new UnsupportedOperationException("Expected a read only instance!");
        }

        @Override
        public @Nullable PermissionAttachment addAttachment(@NotNull final Plugin plugin, final int ticks) {
            throw new UnsupportedOperationException("Expected a read only instance!");
        }

        @Override
        public void removeAttachment(@NotNull final PermissionAttachment attachment) {
            throw new UnsupportedOperationException("Expected a read only instance!");
        }

        @Override
        public void recalculatePermissions() {
        }

        @Override
        public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
            return Set.of();
        }

        @Override
        public boolean isOp() {
            return false;
        }

        @Override
        public void setOp(final boolean value) {
            throw new UnsupportedOperationException("Expected a read only instance!");
        }
    }

}

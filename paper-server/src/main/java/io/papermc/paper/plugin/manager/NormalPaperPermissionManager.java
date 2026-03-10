package io.papermc.paper.plugin.manager;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class NormalPaperPermissionManager extends PaperPermissionManager {

    private final Map<String, Permission> permissions = new HashMap<>();
    private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<>();
    private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<>();
    private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<>();
    private Permissible sharedEntityPermissible;

    public NormalPaperPermissionManager() {
        this.defaultPerms().put(true, new LinkedHashSet<>());
        this.defaultPerms().put(false, new LinkedHashSet<>());
    }

    @Override
    public Map<String, Permission> permissions() {
        return this.permissions;
    }

    @Override
    public Map<Boolean, Set<Permission>> defaultPerms() {
        return this.defaultPerms;
    }

    @Override
    public Map<String, Map<Permissible, Boolean>> permSubs() {
        return this.permSubs;
    }

    @Override
    public Map<Boolean, Map<Permissible, Boolean>> defSubs() {
        return this.defSubs;
    }

    @Override
    public Permissible createPermissible(final ServerOperator operator) {
        if (operator instanceof Player player) {
            return new PermissibleBase(player);
        } else {
            // Has to be lazy since it recalculated default permissions in the constructor
            if (this.sharedEntityPermissible == null) {
                 this.sharedEntityPermissible = new PermissibleBase(new ServerOperator() {
                    @Override
                    public boolean isOp() {
                        return false;
                    }

                    @Override
                    public void setOp(final boolean value) {

                    }
                });
            }

            return this.sharedEntityPermissible;
        }
    }

    @Override
    public Permissible createCommandBlockPermissible() {
        return new PermissibleBase(new ServerOperator() {

            @Override
            public boolean isOp() {
                return true;
            }

            @Override
            public void setOp(boolean value) {
                throw new UnsupportedOperationException("Cannot change operator status of a block");
            }
        });
    }

    @Override
    public CompletableFuture<Optional<Permissible>> loadPlayerPermissible(@NotNull UUID playerUuid) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

}

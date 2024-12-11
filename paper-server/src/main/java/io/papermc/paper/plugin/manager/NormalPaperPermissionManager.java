package io.papermc.paper.plugin.manager;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class NormalPaperPermissionManager extends PaperPermissionManager {

    private final Map<String, Permission> permissions = new HashMap<>();
    private final Map<Boolean, Set<Permission>> defaultPerms = new LinkedHashMap<>();
    private final Map<String, Map<Permissible, Boolean>> permSubs = new HashMap<>();
    private final Map<Boolean, Map<Permissible, Boolean>> defSubs = new HashMap<>();

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
}
